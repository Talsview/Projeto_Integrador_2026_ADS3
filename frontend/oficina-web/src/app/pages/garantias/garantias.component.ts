import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, Observable, switchMap } from 'rxjs';
import { GarantiaApiService } from '../../core/services/garantia-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { AcionamentoGarantia, GarantiaPeca, GarantiaServico, ResponsabilidadeGarantiaPeca } from '../../models/garantia.model';
import { OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';

type TipoGarantiaModal = 'PECA' | 'SERVICO';
type AcaoGarantiaModal = 'ACIONAR' | 'ENCERRAR';

@Component({ selector: 'app-garantias', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './garantias.component.html' })
export class GarantiasComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  garantiasPecas: GarantiaPeca[] = [];
  garantiasServicos: GarantiaServico[] = [];
  idOrdemSelecionada = 0;
  readonly statusPermitidoFiltro: StatusFluxoOrdemServico = 'FINALIZADO';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;

  modalAberto = false;
  tipoModal?: TipoGarantiaModal;
  acaoModal?: AcaoGarantiaModal;
  garantiaSelecionada?: GarantiaPeca | GarantiaServico;
  formGarantia: AcionamentoGarantia = this.novoFormulario();
  errosCampo: Record<string, string> = {};
  responsabilidades: ResponsabilidadeGarantiaPeca[] = ['FORNECEDOR', 'OFICINA', 'AMBOS'];

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(private readonly garantiaApi: GarantiaApiService, private readonly ordemApi: OrdemServicoApiService, private readonly cdr: ChangeDetectorRef) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void { this.carregarTelaInicial(); }

  carregarTelaInicial(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    /**
     * Função: Controla na tela a etapa fork join.
     * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
     */
    forkJoin({ ordens: this.ordemApi.listar(), pecas: this.garantiaApi.listarGarantiasPecas(), servicos: this.garantiaApi.listarGarantiasServicos() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: r => { this.ordens = this.filtrarOrdensPorStatus(r.ordens, this.statusPermitidoFiltro); this.garantiasPecas = [...r.pecas]; this.garantiasServicos = [...r.servicos]; this.atualizarTela(); },
        error: e => { this.erro = e.message; this.atualizarTela(); }
      });
  }

  listarTodas(): void { this.idOrdemSelecionada = 0; this.carregarTelaInicial(); }

  /**
   * Função: Controla na tela a etapa listar por ordem.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  listarPorOrdem(): void {
    if (!this.idOrdemSelecionada) { this.listarTodas(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    /**
     * Função: Controla na tela a etapa fork join.
     * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
     */
    forkJoin({ pecas: this.garantiaApi.listarPecasPorOrdemServico(this.idOrdemSelecionada), servicos: this.garantiaApi.listarServicosPorOrdemServico(this.idOrdemSelecionada) })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({ next: r => { this.garantiasPecas = [...r.pecas]; this.garantiasServicos = [...r.servicos]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } });
  }

  /**
   * Função: Controla na tela a etapa abrir acionamento peca.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirAcionamentoPeca(g: GarantiaPeca): void { this.abrirModal('PECA', 'ACIONAR', g); }
  abrirEncerramentoPeca(g: GarantiaPeca): void { this.abrirModal('PECA', 'ENCERRAR', g); }
  /**
   * Função: Controla na tela a etapa abrir acionamento servico.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirAcionamentoServico(g: GarantiaServico): void { this.abrirModal('SERVICO', 'ACIONAR', g); }
  abrirEncerramentoServico(g: GarantiaServico): void { this.abrirModal('SERVICO', 'ENCERRAR', g); }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  abrirModal(tipo: TipoGarantiaModal, acao: AcaoGarantiaModal, garantia: GarantiaPeca | GarantiaServico): void {
    if (acao === 'ACIONAR' && !this.permiteAcionar(garantia.statusGarantia)) {
      this.erro = 'Apenas garantias vigentes podem ser acionadas.';
      return;
    }
    if (acao === 'ENCERRAR' && !this.permiteEncerrar(garantia.statusGarantia)) {
      this.erro = 'Apenas garantias acionadas podem ser encerradas.';
      return;
    }
    this.tipoModal = tipo;
    this.acaoModal = acao;
    this.garantiaSelecionada = garantia;
    this.formGarantia = this.novoFormulario(garantia, acao, tipo);
    this.errosCampo = {};
    this.modalAberto = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
  }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  fecharModal(): void {
    if (this.processando) return;
    this.limparModalAposSucesso();
    this.atualizarTela();
  }

  /**
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
  private limparModalAposSucesso(): void {
    this.modalAberto = false;
    this.tipoModal = undefined;
    this.acaoModal = undefined;
    this.garantiaSelecionada = undefined;
    this.formGarantia = this.novoFormulario();
    this.errosCampo = {};
  }

  /**
   * Função: Controla na tela a etapa confirmar atendimento garantia.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  confirmarAtendimentoGarantia(): void {
    if (!this.garantiaSelecionada?.id || !this.tipoModal || !this.acaoModal) return;
    if (!this.validarFormularioGarantia()) return;

    const id = this.garantiaSelecionada.id;
    const payload: AcionamentoGarantia = { ...this.formGarantia };
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    const acao$: Observable<GarantiaPeca | GarantiaServico> = this.tipoModal === 'PECA'
      ? (this.acaoModal === 'ACIONAR' ? this.garantiaApi.acionarGarantiaPeca(id, payload) : this.garantiaApi.encerrarGarantiaPeca(id, payload))
      : (this.acaoModal === 'ACIONAR' ? this.garantiaApi.acionarGarantiaServico(id, payload) : this.garantiaApi.encerrarGarantiaServico(id, payload));

    acao$.pipe(
      switchMap((): Observable<GarantiaPeca[] | GarantiaServico[]> => this.recarregarGarantiasDoTipoAtual()),
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: garantias => {
        if (this.tipoModal === 'PECA') {
          this.garantiasPecas = [...(garantias as GarantiaPeca[])];
        } else {
          this.garantiasServicos = [...(garantias as GarantiaServico[])];
        }
        this.mensagem = this.acaoModal === 'ACIONAR'
          ? 'Garantia acionada.'
          : 'Garantia encerrada.';
        this.limparModalAposSucesso();
        this.atualizarTela();
      },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  permiteAcionar(status?: string): boolean { return status === 'VIGENTE'; }
  permiteEncerrar(status?: string): boolean { return status === 'ACIONADA'; }

  /**
   * Função: Controla na tela a etapa nome garantia selecionada.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  nomeGarantiaSelecionada(): string {
    if (!this.garantiaSelecionada) return '';
    if (this.tipoModal === 'PECA') {
      return (this.garantiaSelecionada as GarantiaPeca).nomePeca || 'Peça não informada';
    }
    return (this.garantiaSelecionada as GarantiaServico).nomeServico || 'Serviço não informado';
  }

  /**
   * Função: apresenta quem executou o serviço que gerou a garantia.
   * Uso no sistema: se o serviço for interno, mostra o colaborador da oficina; se for
   * terceirizado, mostra a empresa externa responsável pela execução.
   */
  rotuloExecutorServico(g: GarantiaServico): string {
    if (g.tipoExecucaoServico === 'TERCEIRIZADO') {
      return g.nomeEmpresaTerceirizada || 'Empresa terceirizada não informada';
    }
    return g.nomeColaboradorResponsavel || 'Colaborador não informado';
  }

  /**
   * Função: detalha o tipo de execução vinculado à garantia de serviço.
   * Uso no sistema: deixa claro na tela de garantias se a responsabilidade operacional
   * veio de um colaborador interno ou de uma empresa terceirizada.
   */
  detalheExecutorServico(g: GarantiaServico): string {
    if (g.tipoExecucaoServico === 'TERCEIRIZADO') {
      const envio = g.dataEnvioTerceirizacao ? `Envio: ${this.formatarDataSimples(g.dataEnvioTerceirizacao)}` : 'Envio não informado';
      const retorno = g.dataRetornoTerceirizacao ? `Retorno: ${this.formatarDataSimples(g.dataRetornoTerceirizacao)}` : 'Retorno não informado';
      return `Terceirizado — ${envio} — ${retorno}`;
    }
    return 'Interno — colaborador da oficina';
  }

  /**
   * Função: monta o texto de executor para o modal da garantia selecionada.
   * Uso no sistema: reforça, antes de acionar ou encerrar a garantia, quem executou o serviço original.
   */
  detalheExecutorGarantiaSelecionada(): string {
    if (!this.garantiaSelecionada || this.tipoModal !== 'SERVICO') return '';
    const garantia = this.garantiaSelecionada as GarantiaServico;
    return `Execução: ${this.rotuloExecutorServico(garantia)} (${this.detalheExecutorServico(garantia)})`;
  }

  /**
   * Função: Controla na tela a etapa classe status.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  classeStatus(status?: string): string {
    if (status === 'VIGENTE') return 'success';
    if (status === 'ACIONADA') return 'warning';
    if (status === 'ENCERRADA') return 'info';
    if (status === 'EXPIRADA') return 'danger';
    return '';
  }

  /**
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
  limparErro(campo: string): void { delete this.errosCampo[campo]; }

  private recarregarGarantiasDoTipoAtual(): Observable<GarantiaPeca[] | GarantiaServico[]> {
    if (this.tipoModal === 'PECA') {
      return this.idOrdemSelecionada ? this.garantiaApi.listarPecasPorOrdemServico(this.idOrdemSelecionada) : this.garantiaApi.listarGarantiasPecas();
    }
    return this.idOrdemSelecionada ? this.garantiaApi.listarServicosPorOrdemServico(this.idOrdemSelecionada) : this.garantiaApi.listarGarantiasServicos();
  }

  /**
   * Função: Controla na tela a etapa validar formulario garantia.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormularioGarantia(): boolean {
    this.errosCampo = {};
    const hoje = this.dataHoje();

    if (this.acaoModal === 'ACIONAR') {
      if (!this.valorTexto(this.formGarantia.dataAcionamento)) this.errosCampo['dataAcionamento'] = 'Informe a data do acionamento.';
      if (this.formGarantia.dataAcionamento && this.formGarantia.dataAcionamento > hoje) this.errosCampo['dataAcionamento'] = 'A data do acionamento não pode ser futura.';
      if (this.garantiaSelecionada?.dataInicio && this.formGarantia.dataAcionamento && this.formGarantia.dataAcionamento < this.garantiaSelecionada.dataInicio) {
        this.errosCampo['dataAcionamento'] = 'A data do acionamento não pode ser anterior ao início da garantia.';
      }
      if (!this.valorTexto(this.formGarantia.motivoAcionamento)) this.errosCampo['motivoAcionamento'] = 'Informe o motivo do acionamento.';
      if (!this.valorTexto(this.formGarantia.descricaoDefeito)) this.errosCampo['descricaoDefeito'] = 'Informe o defeito relatado pelo cliente.';
      if (!this.valorTexto(this.formGarantia.responsavelAnalise)) this.errosCampo['responsavelAnalise'] = 'Informe o responsável pela análise.';
      if (this.tipoModal === 'PECA' && !this.formGarantia.responsabilidade) this.errosCampo['responsabilidade'] = 'Informe a responsabilidade inicial.';
    }

    if (this.acaoModal === 'ENCERRAR') {
      if (!this.valorTexto(this.formGarantia.dataEncerramento)) this.errosCampo['dataEncerramento'] = 'Informe a data do encerramento.';
      if (this.formGarantia.dataEncerramento && this.formGarantia.dataEncerramento > hoje) this.errosCampo['dataEncerramento'] = 'A data do encerramento não pode ser futura.';
      const dataAcionamento = this.garantiaSelecionada?.dataAcionamento;
      if (dataAcionamento && this.formGarantia.dataEncerramento && this.formGarantia.dataEncerramento < dataAcionamento) {
        this.errosCampo['dataEncerramento'] = 'A data do encerramento não pode ser anterior ao acionamento.';
      }
      if (!this.valorTexto(this.formGarantia.solucaoAplicada)) this.errosCampo['solucaoAplicada'] = 'Informe a solução aplicada.';
      if (!this.valorTexto(this.formGarantia.custoAssumidoPor)) this.errosCampo['custoAssumidoPor'] = 'Informe quem assumiu o custo.';
    }

    this.atualizarTela();
    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: Controla na tela a etapa novo formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private novoFormulario(garantia?: GarantiaPeca | GarantiaServico, acao?: AcaoGarantiaModal, tipo?: TipoGarantiaModal): AcionamentoGarantia {
    const hoje = this.dataHoje();
    if (acao === 'ENCERRAR') {
      return {
        dataEncerramento: hoje,
        atendimentoRealizado: true,
        custoAssumidoPor: tipo === 'PECA' ? 'FORNECEDOR' : 'OFICINA'
      };
    }
    return {
      dataAcionamento: hoje,
      responsabilidade: tipo === 'PECA' ? ((garantia as GarantiaPeca)?.responsabilidade || 'FORNECEDOR') : undefined
    };
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  private filtrarOrdensPorStatus(ordens: OrdemServicoResumo[], status: StatusFluxoOrdemServico): OrdemServicoResumo[] {
    return [...(ordens ?? [])].filter(os => this.normalizarStatus(os.statusAtual) === status);
  }

  /**
   * Função: Controla na tela a etapa normalizar status.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private normalizarStatus(status?: string): StatusFluxoOrdemServico | '' {
    return (status ?? '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toUpperCase()
      .replace(/\s+/g, '_') as StatusFluxoOrdemServico | '';
  }

  /**
   * Função: Controla na tela a etapa data hoje.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private dataHoje(): string { return new Date().toISOString().substring(0, 10); }

  private formatarDataSimples(valor?: string): string {
    if (!valor) return '-';
    const data = valor.substring(0, 10).split('-');
    return data.length === 3 ? `${data[2]}/${data[1]}/${data[0]}` : valor;
  }

  private valorTexto(value?: string): boolean { return !!value && value.trim().length > 0; }
  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
