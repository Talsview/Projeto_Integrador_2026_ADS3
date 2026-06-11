import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { dataHoraAnterior, dataHoraFutura, numeroMaiorQueZero } from '../../core/validation/field-validation';
import { PagamentoApiService } from '../../core/services/pagamento-api.service';
import { OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';
import { FormaPagamento, Pagamento, ResumoPagamentoOrdemServico, StatusPagamento } from '../../models/pagamento.model';

@Component({
  selector: 'app-pagamentos',
  standalone: true,
  imports: [CommonModule, FormsModule, InativosPanelComponent],
  templateUrl: './pagamentos.component.html'
})
export class PagamentosComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  pagamentos: Pagamento[] = [];
  pagamentosInativos: Pagamento[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  resumo?: ResumoPagamentoOrdemServico;
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  idOrdemSelecionada = 0;
  readonly statusPermitido: StatusFluxoOrdemServico = 'PAGAMENTO';

  formas: FormaPagamento[] = ['DINHEIRO', 'PIX', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'TRANSFERENCIA', 'BOLETO', 'OUTRO'];
  status: StatusPagamento[] = ['PENDENTE', 'PAGO', 'CANCELADO', 'ESTORNADO'];

  form: Pagamento = this.criarFormularioInicial();

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly ordemApi: OrdemServicoApiService,
    private readonly pagamentoApi: PagamentoApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void {
    this.carregarOrdens();
  }

  get ordemSelecionada(): OrdemServicoResumo | undefined {
    return this.ordens.find(ordem => ordem.id === Number(this.idOrdemSelecionada));
  }

  get statusAtualOs(): string {
    return this.ordemSelecionada?.statusAtual ?? 'NÃO INFORMADO';
  }

  get statusPermiteNovoPagamento(): boolean {
    return this.normalizarStatus(this.statusAtualOs) === this.statusPermitido;
  }

  /**
   * Função: Controla na tela a etapa carregar ordens.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarOrdens(): void {
    this.carregando = true;
    this.erro = undefined;

    this.ordemApi.listar()
      .pipe(finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: ordens => {
          this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusPermitido);
          if (this.idOrdemSelecionada && !this.ordemSelecionada) {
            this.idOrdemSelecionada = 0;
            this.pagamentos = [];
            this.resumo = undefined;
          }
          this.cdr.detectChanges();
        },
        error: error => this.erro = error.message
      });
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  aoSelecionarOrdem(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.limparForm();
    this.carregarPagamentos();
  }

  /**
   * Função: Aciona a mudança de etapa da Ordem de Serviço conforme o fluxo operacional permitido.
   * Uso no sistema: impede salto indevido entre Orçamento, Execução, Pagamento e Finalizado.
   */
  carregarPagamentos(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!this.idOrdemSelecionada) {
      this.pagamentos = [];
      this.resumo = undefined;
      this.limparForm();
      this.cdr.detectChanges();
      return;
    }

    this.carregando = true;
    this.form.idOrdemServico = this.idOrdemSelecionada;

    forkJoin({
      ordens: this.ordemApi.listar(),
      pagamentos: this.pagamentoApi.listarPorOrdemServico(this.idOrdemSelecionada),
      resumo: this.pagamentoApi.resumoPorOrdemServico(this.idOrdemSelecionada)
    })
      .pipe(finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: resultado => {
          this.ordens = this.filtrarOrdensPorStatus(resultado.ordens, this.statusPermitido);
          this.pagamentos = [...resultado.pagamentos];
          this.resumo = { ...resultado.resumo };
          this.atualizarValorAutomaticoDoFormulario();
          this.cdr.detectChanges();
        },
        error: error => this.erro = error.message
      });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!this.idOrdemSelecionada) {
      this.erro = 'Selecione uma Ordem de Serviço antes de registrar pagamento.';
      return;
    }

    if (!this.statusPermiteNovoPagamento && !this.form.id) {
      this.erro = 'Nesta tela só é possível registrar pagamento de OS no status PAGAMENTO.';
      return;
    }

    const erroValidacao = this.validarFormularioPagamento();
    if (erroValidacao) {
      this.erro = erroValidacao;
      this.cdr.detectChanges();
      return;
    }

    const valorPendente = Number(this.resumo?.valorPendente ?? 0);
    const valorInformado = Number(this.form.valorPago ?? 0);

    const payload: Pagamento = {
      ...this.form,
      idOrdemServico: this.idOrdemSelecionada,
      valorPago: valorInformado > 0 ? valorInformado : valorPendente,
      statusPagamento: this.form.statusPagamento ?? 'PAGO'
    };

    const acao = payload.id
      ? this.pagamentoApi.atualizar(payload.id, payload)
      : this.pagamentoApi.criar(payload);

    this.processando = true;
    acao.pipe(finalize(() => {
      this.processando = false;
      this.cdr.detectChanges();
    })).subscribe({
      next: () => {
        this.mensagem = 'Pagamento salvo.';
        this.limparForm();
        this.carregarPagamentos();
      },
      error: error => this.erro = error.message
    });
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  editar(pagamento: Pagamento): void {
    this.form = { ...pagamento };
    this.idOrdemSelecionada = pagamento.idOrdemServico ?? this.idOrdemSelecionada;
    this.cdr.detectChanges();
  }

  /**
   * Função: Controla na tela a etapa alterar status.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  alterarStatus(pagamento: Pagamento, statusPagamento: StatusPagamento): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!pagamento.id) return;

    this.processando = true;
    this.pagamentoApi.alterarStatus(pagamento.id, statusPagamento)
      .pipe(finalize(() => {
        this.processando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: () => {
          this.mensagem = 'Status atualizado.';
          this.carregarPagamentos();
        },
        error: error => this.erro = error.message
      });
  }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluir(pagamento: Pagamento): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!pagamento.id) return;

    this.processando = true;
    this.pagamentoApi.excluir(pagamento.id)
      .pipe(finalize(() => {
        this.processando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: () => {
          this.mensagem = 'Pagamento inativado.';
          this.carregarPagamentos();
        },
        error: error => this.erro = error.message
      });
  }


  /**
   * Função: Controla na tela a etapa abrir inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirInativos(): void {
    this.mostrarInativos = true;
    this.carregarInativos();
  }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  fecharInativos(): void {
    this.mostrarInativos = false;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa carregar inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarInativos(): void {
    this.carregandoInativos = true;
    this.erro = undefined;
    this.atualizarTela();
    this.pagamentoApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.pagamentosInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: Pagamento): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.pagamentoApi.ativar(registro.id)
      .pipe(switchMap(() => this.pagamentoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.pagamentos = [...registros];
          this.mensagem = 'Cadastro ativado.';
          this.carregarInativos();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível ativar o cadastro.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
  limparForm(): void {
    this.form = this.criarFormularioInicial();
    this.form.idOrdemServico = this.idOrdemSelecionada;
    this.atualizarValorAutomaticoDoFormulario();
  }


  /**
   * Função: força a atualização visual da tela de pagamentos após mudanças assíncronas.
   * Uso no sistema: este componente abre painel de inativos, carrega dados por API e altera
   * listas fora do fluxo imediato do clique do usuário; por isso, este método centraliza o
   * `detectChanges()` do Angular e evita repetir a chamada diretamente em todos os pontos da tela.
   */
  private atualizarTela(): void {
    this.cdr.detectChanges();
  }

  /**
   * Função: Controla na tela a etapa classe status os.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  classeStatusOs(status?: string): string {
    if (status === 'PAGAMENTO') return 'warning';
    if (status === 'FINALIZADO') return 'success';
    if (status === 'EXECUCAO') return 'warning';
    return '';
  }

  /**
   * Função: Aciona a mudança de etapa da Ordem de Serviço conforme o fluxo operacional permitido.
   * Uso no sistema: impede salto indevido entre Orçamento, Execução, Pagamento e Finalizado.
   */
  private validarFormularioPagamento(): string | undefined {
    if (!this.form.formaPagamento) return 'Selecione a forma de pagamento.';
    if (!this.form.statusPagamento) return 'Selecione o status do pagamento.';
    if (!numeroMaiorQueZero(this.form.valorPago)) return 'O valor do pagamento deve ser maior que zero.';
    const valorPendente = Number(this.resumo?.valorPendente ?? 0);
    const valorInformado = Number(this.form.valorPago ?? 0);
    if (!this.form.id && valorPendente >= 0 && valorInformado > valorPendente) return 'O valor pago não pode ser maior que o saldo pendente da OS.';
    if (dataHoraFutura(this.form.dataPagamento as any)) return 'A data de pagamento não pode ser futura.';
    const ordem = this.ordemSelecionada as any;
    if (ordem?.dataAbertura && dataHoraAnterior(this.form.dataPagamento as any, ordem.dataAbertura)) return 'A data de pagamento não pode ser anterior à data de abertura da OS.';
    if ((this.form.statusPagamento === 'CANCELADO' || this.form.statusPagamento === 'ESTORNADO') && this.form.dataPagamento) return 'Pagamento cancelado ou estornado não deve possuir data de pagamento efetivo.';
    return undefined;
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
   * Função: Controla na tela a etapa criar formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private criarFormularioInicial(): Pagamento {
    return {
      idOrdemServico: this.idOrdemSelecionada,
      formaPagamento: 'PIX',
      valorPago: 0,
      dataPagamento: this.agoraParaInputDateTimeLocal(),
      statusPagamento: 'PAGO',
      observacao: ''
    };
  }

  /**
   * Função: Controla na tela a etapa atualizar valor automatico do formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarValorAutomaticoDoFormulario(): void {
    if (this.form.id) {
      return;
    }
    const valorPendente = Number(this.resumo?.valorPendente ?? 0);
    this.form.valorPago = valorPendente > 0 ? valorPendente : 0;
    if (!this.form.dataPagamento) {
      this.form.dataPagamento = this.agoraParaInputDateTimeLocal();
    }
  }

  /**
   * Função: Controla na tela a etapa agora para input date time local.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private agoraParaInputDateTimeLocal(): string {
    const agora = new Date();
    agora.setMinutes(agora.getMinutes() - agora.getTimezoneOffset());
    return agora.toISOString().slice(0, 16);
  }
}
