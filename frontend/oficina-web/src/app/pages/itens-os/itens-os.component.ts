import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { FornecedorApiService } from '../../core/services/fornecedor-api.service';
import { ItemPecaApiService } from '../../core/services/item-peca-api.service';
import { ItemServicoApiService } from '../../core/services/item-servico-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { PecaApiService } from '../../core/services/peca-api.service';
import { ServicoApiService } from '../../core/services/servico-api.service';
import { dataHoraAnterior, dataHoraFutura, numeroMaiorQueZero, numeroNaoNegativo } from '../../core/validation/field-validation';
import { ColaboradorResumo } from '../../models/pessoa.model';
import { Fornecedor, ItemPeca, Peca } from '../../models/peca.model';
import { EmpresaTerceirizada, Servico } from '../../models/servico.model';
import { ItemServico, OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';

@Component({ selector: 'app-itens-os', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './itens-os.component.html' })
export class ItensOsComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  servicos: Servico[] = [];
  colaboradores: ColaboradorResumo[] = [];
  empresas: EmpresaTerceirizada[] = [];
  pecas: Peca[] = [];
  fornecedores: Fornecedor[] = [];
  itensServico: ItemServico[] = [];
  itensPeca: ItemPeca[] = [];
  readonly statusPermitido: StatusFluxoOrdemServico = 'ORCAMENTO';
  idOrdemSelecionada = 0;
  carregando = false;
  processando = false;
  mensagem?: string;
  erro?: string;
  errosServico: Record<string, string> = {};
  errosPeca: Record<string, string> = {};

  itemServicoForm: ItemServico = this.itemServicoInicial();
  itemPecaForm: ItemPeca = this.itemPecaInicial();

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly ordemApi: OrdemServicoApiService,
    private readonly servicoApi: ServicoApiService,
    private readonly colaboradorApi: ColaboradorApiService,
    private readonly empresaApi: EmpresaTerceirizadaApiService,
    private readonly pecaApi: PecaApiService,
    private readonly fornecedorApi: FornecedorApiService,
    private readonly itemServicoApi: ItemServicoApiService,
    private readonly itemPecaApi: ItemPecaApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void { this.carregarApoio(); }

  carregarApoio(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    forkJoin({
      ordens: this.ordemApi.listar(),
      servicos: this.servicoApi.listar(),
      colaboradores: this.colaboradorApi.listar(),
      empresas: this.empresaApi.listar(),
      pecas: this.pecaApi.listar(),
      fornecedores: this.fornecedorApi.listar()
    }).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: r => { this.ordens = this.filtrarOrdensPorStatus(r.ordens, this.statusPermitido); this.servicos = [...r.servicos]; this.colaboradores = [...r.colaboradores]; this.empresas = [...r.empresas]; this.pecas = [...r.pecas]; this.fornecedores = [...r.fornecedores]; if (this.idOrdemSelecionada && !this.ordemSelecionadaEhStatusPermitido()) { this.limparSelecaoOrdem(); } this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Controla na tela a etapa carregar itens.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarItens(): void {
    this.mensagem = undefined; this.erro = undefined;
    if (!this.idOrdemSelecionada) { this.itensServico = []; this.itensPeca = []; this.atualizarTela(); return; }
    if (!this.ordemSelecionadaEhStatusPermitido()) { this.erro = 'Nesta tela aparecem apenas Ordens de Serviço em ORÇAMENTO. Para incluir serviços e peças, selecione uma OS nessa etapa do fluxo.'; this.itensServico = []; this.itensPeca = []; this.atualizarTela(); return; }
    this.carregando = true; this.atualizarTela();
    forkJoin({
      servicos: this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada),
      pecas: this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada)
    }).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: r => { this.itensServico = [...r.servicos]; this.itensPeca = [...r.pecas]; this.itemServicoForm.idOrdemServico = this.idOrdemSelecionada; this.itemPecaForm.idOrdemServico = this.idOrdemSelecionada; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvarItemServico(): void {
    if (!this.idOrdemSelecionada) { this.erro = 'Selecione uma OS antes de incluir serviço.'; this.atualizarTela(); return; }
    if (!this.ordemSelecionadaEhStatusPermitido()) { this.erro = 'Serviços só podem ser incluídos enquanto a OS está em ORÇAMENTO.'; this.atualizarTela(); return; }
    if (!this.validarItemServico()) { this.erro = 'Corrija os campos destacados antes de adicionar o serviço à OS.'; this.atualizarTela(); return; }
    this.recalcularTotalServico(false);
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const payload = { ...this.itemServicoForm, idOrdemServico: this.idOrdemSelecionada };
    this.itemServicoApi.criar(payload).pipe(switchMap(() => this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: itens => { this.mensagem = 'Serviço incluído.'; this.itensServico = [...itens]; this.itemServicoForm = this.itemServicoInicial(); this.itemServicoForm.idOrdemServico = this.idOrdemSelecionada; this.errosServico = {}; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvarItemPeca(): void {
    if (!this.idOrdemSelecionada) { this.erro = 'Selecione uma OS antes de incluir peça.'; this.atualizarTela(); return; }
    if (!this.ordemSelecionadaEhStatusPermitido()) { this.erro = 'Peças só podem ser incluídas enquanto a OS está em ORÇAMENTO.'; this.atualizarTela(); return; }
    if (!this.validarItemPeca()) { this.erro = 'Corrija os campos destacados antes de adicionar a peça à OS.'; this.atualizarTela(); return; }
    this.recalcularTotalPeca(false);
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const payload = { ...this.itemPecaForm, idOrdemServico: this.idOrdemSelecionada };
    this.itemPecaApi.criar(payload).pipe(switchMap(() => this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: itens => { this.mensagem = 'Peça incluída.'; this.itensPeca = [...itens]; this.itemPecaForm = this.itemPecaInicial(); this.itemPecaForm.idOrdemServico = this.idOrdemSelecionada; this.errosPeca = {}; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Controla na tela a etapa servico selecionado terceirizado.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  servicoSelecionadoTerceirizado(): boolean {
    const servico = this.servicos.find(s => Number(s.id) === Number(this.itemServicoForm.idServico));
    return servico?.tipoServico === 'TERCEIRIZADO';
  }

  /**
   * Função: Controla na tela a etapa ao alterar servico.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  aoAlterarServico(): void {
    const servico = this.servicos.find(s => Number(s.id) === Number(this.itemServicoForm.idServico));
    if (servico) {
      this.itemServicoForm.valorUnitario = Number(servico.valorBase ?? 0);
      this.recalcularTotalServico(false);
    }
    if (!this.servicoSelecionadoTerceirizado()) {
      this.itemServicoForm.idEmpresaTerceirizada = undefined;
      this.itemServicoForm.dataEnvioTerceirizacao = undefined;
      this.itemServicoForm.dataRetornoTerceirizacao = undefined;
      this.itemServicoForm.valorCobradoTerceirizacao = undefined;
      this.itemServicoForm.observacaoTerceirizacao = undefined;
    }
    delete this.errosServico['idServico'];
    delete this.errosServico['valorUnitario'];
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa ao alterar peca.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  aoAlterarPeca(): void {
    const peca = this.pecas.find(p => Number(p.id) === Number(this.itemPecaForm.idPeca));
    if (peca) {
      this.itemPecaForm.idFornecedor = Number(peca.idFornecedorPadrao ?? 0);
      this.itemPecaForm.valorUnitario = Number(peca.valorUnitarioPadrao ?? 0);
      this.recalcularTotalPeca(false);
    } else {
      this.itemPecaForm.idFornecedor = 0;
      this.itemPecaForm.valorUnitario = 0;
      this.itemPecaForm.valorTotal = 0;
    }
    delete this.errosPeca['idPeca'];
    delete this.errosPeca['idFornecedor'];
    delete this.errosPeca['valorUnitario'];
    this.atualizarTela();
  }

  /**
   * Função: Recalcula valores exibidos na tela conforme quantidade, peça, serviço ou valor unitário
   * informado.
   * Uso no sistema: mantém o orçamento visual coerente antes de enviar os itens para a API.
   */
  recalcularTotalServico(atualizar = true): void {
    const quantidade = Number(this.itemServicoForm.quantidade ?? 0);
    const valorUnitario = Number(this.itemServicoForm.valorUnitario ?? 0);
    this.itemServicoForm.valorTotal = quantidade > 0 && valorUnitario >= 0 ? quantidade * valorUnitario : 0;
    if (atualizar) this.atualizarTela();
  }

  /**
   * Função: Recalcula valores exibidos na tela conforme quantidade, peça, serviço ou valor unitário
   * informado.
   * Uso no sistema: mantém o orçamento visual coerente antes de enviar os itens para a API.
   */
  recalcularTotalPeca(atualizar = true): void {
    const quantidade = Number(this.itemPecaForm.quantidade ?? 0);
    const valorUnitario = Number(this.itemPecaForm.valorUnitario ?? 0);
    this.itemPecaForm.valorTotal = quantidade > 0 && valorUnitario >= 0 ? quantidade * valorUnitario : 0;
    if (atualizar) this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa fornecedor peca selecionado.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  fornecedorPecaSelecionado(): string {
    const fornecedor = this.fornecedores.find(f => Number(f.id) === Number(this.itemPecaForm.idFornecedor));
    return fornecedor?.nomeFornecedor ?? 'Nenhum fornecedor vinculado à peça selecionada';
  }

  excluirItemServico(item: ItemServico): void { if (!item.id) return; this.processando = true; this.atualizarTela(); this.itemServicoApi.excluir(item.id).pipe(switchMap(() => this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: itens => { this.itensServico = [...itens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  excluirItemPeca(item: ItemPeca): void { if (!item.id) return; this.processando = true; this.atualizarTela(); this.itemPecaApi.excluir(item.id).pipe(switchMap(() => this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: itens => { this.itensPeca = [...itens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

  /**
   * Função: Aciona a mudança de etapa da Ordem de Serviço conforme o fluxo operacional permitido.
   * Uso no sistema: impede salto indevido entre Orçamento, Execução, Pagamento e Finalizado.
   */
  enviarOrcamentoParaExecucao(): void {
    if (!this.idOrdemSelecionada) {
      this.erro = 'Selecione uma OS em orçamento antes de enviar para execução.';
      this.atualizarTela();
      return;
    }
    if (!this.ordemSelecionadaEhStatusPermitido()) {
      this.erro = 'Somente Ordens de Serviço em ORÇAMENTO podem ser enviadas para execução por esta tela.';
      this.atualizarTela();
      return;
    }
    if (!this.itensServico.length) {
      this.erro = 'Inclua pelo menos um serviço no orçamento antes de enviar a OS para execução.';
      this.atualizarTela();
      return;
    }

    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    this.ordemApi.enviarOrcamentoParaExecucao(this.idOrdemSelecionada)
      .pipe(
        switchMap(() => this.ordemApi.listar()),
        finalize(() => { this.processando = false; this.atualizarTela(); })
      )
      .subscribe({
        next: ordens => {
          this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusPermitido);
          this.mensagem = 'Enviado para execução.';
          this.limparSelecaoOrdem();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível enviar o orçamento para execução.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Aciona a mudança de etapa da Ordem de Serviço conforme o fluxo operacional permitido.
   * Uso no sistema: impede salto indevido entre Orçamento, Execução, Pagamento e Finalizado.
   */
  podeEnviarOrcamentoParaExecucao(): boolean {
    return !!this.idOrdemSelecionada
      && this.ordemSelecionadaEhStatusPermitido()
      && this.itensServico.length > 0
      && !this.processando;
  }

  /**
   * Função: Recalcula valores exibidos na tela conforme quantidade, peça, serviço ou valor unitário
   * informado.
   * Uso no sistema: mantém o orçamento visual coerente antes de enviar os itens para a API.
   */
  totalOrcamentoSelecionado(): number {
    const totalServicos = this.itensServico.reduce((soma, item) => soma + Number(item.valorTotal ?? 0), 0);
    const totalPecas = this.itensPeca.reduce((soma, item) => soma + Number(item.valorTotal ?? 0), 0);
    return totalServicos + totalPecas;
  }

  /**
   * Função: Controla na tela a etapa validar item servico.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarItemServico(): boolean {
    this.errosServico = {};
    if (!this.itemServicoForm.idServico || Number(this.itemServicoForm.idServico) <= 0) this.errosServico['idServico'] = 'Selecione o serviço da OS.';
    if (!this.itemServicoForm.idColaborador || Number(this.itemServicoForm.idColaborador) <= 0) this.errosServico['idColaborador'] = 'Selecione o colaborador responsável pelo serviço.';
    if (!numeroMaiorQueZero(this.itemServicoForm.quantidade)) this.errosServico['quantidade'] = 'A quantidade do serviço deve ser maior que zero.';
    if (!numeroNaoNegativo(this.itemServicoForm.valorUnitario)) this.errosServico['valorUnitario'] = 'O valor unitário do serviço não pode ser negativo.';
    if (dataHoraFutura(this.itemServicoForm.dataInicio as any)) this.errosServico['dataInicio'] = 'A data de início do serviço não pode ser futura.';
    if (dataHoraAnterior(this.itemServicoForm.dataFim as any, this.itemServicoForm.dataInicio as any)) this.errosServico['dataFim'] = 'A data de fim do serviço não pode ser anterior à data de início.';
    if (this.servicoSelecionadoTerceirizado() && (!this.itemServicoForm.idEmpresaTerceirizada || Number(this.itemServicoForm.idEmpresaTerceirizada) <= 0)) this.errosServico['idEmpresaTerceirizada'] = 'Serviço terceirizado exige empresa terceirizada executora.';
    if (!this.servicoSelecionadoTerceirizado() && this.itemServicoForm.idEmpresaTerceirizada) this.errosServico['idEmpresaTerceirizada'] = 'Serviço interno não deve possuir empresa terceirizada.';
    if (dataHoraFutura(this.itemServicoForm.dataEnvioTerceirizacao as any)) this.errosServico['dataEnvioTerceirizacao'] = 'A data de envio da terceirização não pode ser futura.';
    if (dataHoraFutura(this.itemServicoForm.dataRetornoTerceirizacao as any)) this.errosServico['dataRetornoTerceirizacao'] = 'A data de retorno da terceirização não pode ser futura.';
    if (dataHoraAnterior(this.itemServicoForm.dataRetornoTerceirizacao as any, this.itemServicoForm.dataEnvioTerceirizacao as any)) this.errosServico['dataRetornoTerceirizacao'] = 'A data de retorno da terceirização não pode ser anterior ao envio.';
    if (!numeroNaoNegativo(this.itemServicoForm.valorCobradoTerceirizacao)) this.errosServico['valorCobradoTerceirizacao'] = 'O valor cobrado pela terceirização não pode ser negativo.';
    return Object.keys(this.errosServico).length === 0;
  }

  /**
   * Função: Controla na tela a etapa validar item peca.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarItemPeca(): boolean {
    this.errosPeca = {};
    if (!this.itemPecaForm.idPeca || Number(this.itemPecaForm.idPeca) <= 0) this.errosPeca['idPeca'] = 'Selecione a peça aplicada na OS.';
    if (!this.itemPecaForm.idFornecedor || Number(this.itemPecaForm.idFornecedor) <= 0) this.errosPeca['idFornecedor'] = 'Selecione o fornecedor da peça aplicada.';
    if (!numeroMaiorQueZero(this.itemPecaForm.quantidade)) this.errosPeca['quantidade'] = 'A quantidade da peça deve ser maior que zero.';
    if (!numeroNaoNegativo(this.itemPecaForm.valorUnitario)) this.errosPeca['valorUnitario'] = 'O valor unitário da peça não pode ser negativo.';
    return Object.keys(this.errosPeca).length === 0;
  }

  /**
   * Função: Controla na tela a etapa ordem selecionada eh status permitido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private ordemSelecionadaEhStatusPermitido(): boolean {
    const ordem = this.ordens.find(os => Number(os.id) === Number(this.idOrdemSelecionada));
    return !!ordem && this.normalizarStatus(ordem.statusAtual) === this.statusPermitido;
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
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
  private limparSelecaoOrdem(): void {
    this.idOrdemSelecionada = 0;
    this.itensServico = [];
    this.itensPeca = [];
    this.itemServicoForm = this.itemServicoInicial();
    this.itemPecaForm = this.itemPecaInicial();
  }

  /**
   * Função: Controla na tela a etapa item servico inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private itemServicoInicial(): ItemServico { return { idOrdemServico: this.idOrdemSelecionada, idServico: 0, idColaborador: 0, quantidade: 1, valorUnitario: 0, valorTotal: 0, descricaoExecucao: '' }; }
  private itemPecaInicial(): ItemPeca { return { idOrdemServico: this.idOrdemSelecionada, idPeca: 0, idFornecedor: 0, quantidade: 1, valorUnitario: 0, valorTotal: 0, observacao: '' }; }
  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
