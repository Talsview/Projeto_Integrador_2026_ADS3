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

  salvarItemServico(): void {
    if (!this.idOrdemSelecionada) { this.erro = 'Selecione uma OS antes de incluir serviço.'; this.atualizarTela(); return; }
    if (!this.ordemSelecionadaEhStatusPermitido()) { this.erro = 'Serviços só podem ser incluídos enquanto a OS está em ORÇAMENTO.'; this.atualizarTela(); return; }
    if (!this.validarItemServico()) { this.erro = 'Corrija os campos destacados antes de adicionar o serviço à OS.'; this.atualizarTela(); return; }
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const payload = { ...this.itemServicoForm, idOrdemServico: this.idOrdemSelecionada };
    this.itemServicoApi.criar(payload).pipe(switchMap(() => this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: itens => { this.mensagem = 'Serviço incluído na OS. A lista foi atualizada automaticamente.'; this.itensServico = [...itens]; this.itemServicoForm = this.itemServicoInicial(); this.itemServicoForm.idOrdemServico = this.idOrdemSelecionada; this.errosServico = {}; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  salvarItemPeca(): void {
    if (!this.idOrdemSelecionada) { this.erro = 'Selecione uma OS antes de incluir peça.'; this.atualizarTela(); return; }
    if (!this.ordemSelecionadaEhStatusPermitido()) { this.erro = 'Peças só podem ser incluídas enquanto a OS está em ORÇAMENTO.'; this.atualizarTela(); return; }
    if (!this.validarItemPeca()) { this.erro = 'Corrija os campos destacados antes de adicionar a peça à OS.'; this.atualizarTela(); return; }
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const payload = { ...this.itemPecaForm, idOrdemServico: this.idOrdemSelecionada };
    this.itemPecaApi.criar(payload).pipe(switchMap(() => this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: itens => { this.mensagem = 'Peça incluída na OS. A lista foi atualizada automaticamente.'; this.itensPeca = [...itens]; this.itemPecaForm = this.itemPecaInicial(); this.itemPecaForm.idOrdemServico = this.idOrdemSelecionada; this.errosPeca = {}; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  servicoSelecionadoTerceirizado(): boolean {
    const servico = this.servicos.find(s => Number(s.id) === Number(this.itemServicoForm.idServico));
    return servico?.tipoServico === 'TERCEIRIZADO';
  }

  aoAlterarServico(): void {
    if (!this.servicoSelecionadoTerceirizado()) {
      this.itemServicoForm.idEmpresaTerceirizada = undefined;
      this.itemServicoForm.dataEnvioTerceirizacao = undefined;
      this.itemServicoForm.dataRetornoTerceirizacao = undefined;
      this.itemServicoForm.valorCobradoTerceirizacao = undefined;
      this.itemServicoForm.observacaoTerceirizacao = undefined;
    }
    delete this.errosServico['idServico'];
    this.atualizarTela();
  }

  excluirItemServico(item: ItemServico): void { if (!item.id) return; this.processando = true; this.atualizarTela(); this.itemServicoApi.excluir(item.id).pipe(switchMap(() => this.itemServicoApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: itens => { this.itensServico = [...itens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  excluirItemPeca(item: ItemPeca): void { if (!item.id) return; this.processando = true; this.atualizarTela(); this.itemPecaApi.excluir(item.id).pipe(switchMap(() => this.itemPecaApi.listarPorOrdemServico(this.idOrdemSelecionada)), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: itens => { this.itensPeca = [...itens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

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
          this.mensagem = 'Orçamento enviado para EXECUÇÃO. A OS agora aparece na Fila de Atendimento para execução do serviço.';
          this.limparSelecaoOrdem();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível enviar o orçamento para execução.'; this.atualizarTela(); }
      });
  }

  podeEnviarOrcamentoParaExecucao(): boolean {
    return !!this.idOrdemSelecionada
      && this.ordemSelecionadaEhStatusPermitido()
      && this.itensServico.length > 0
      && !this.processando;
  }

  totalOrcamentoSelecionado(): number {
    const totalServicos = this.itensServico.reduce((soma, item) => soma + Number(item.valorTotal ?? 0), 0);
    const totalPecas = this.itensPeca.reduce((soma, item) => soma + Number(item.valorTotal ?? 0), 0);
    return totalServicos + totalPecas;
  }

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

  private validarItemPeca(): boolean {
    this.errosPeca = {};
    if (!this.itemPecaForm.idPeca || Number(this.itemPecaForm.idPeca) <= 0) this.errosPeca['idPeca'] = 'Selecione a peça aplicada na OS.';
    if (!this.itemPecaForm.idFornecedor || Number(this.itemPecaForm.idFornecedor) <= 0) this.errosPeca['idFornecedor'] = 'Selecione o fornecedor da peça aplicada.';
    if (!numeroMaiorQueZero(this.itemPecaForm.quantidade)) this.errosPeca['quantidade'] = 'A quantidade da peça deve ser maior que zero.';
    if (!numeroNaoNegativo(this.itemPecaForm.valorUnitario)) this.errosPeca['valorUnitario'] = 'O valor unitário da peça não pode ser negativo.';
    return Object.keys(this.errosPeca).length === 0;
  }

  private ordemSelecionadaEhStatusPermitido(): boolean {
    const ordem = this.ordens.find(os => Number(os.id) === Number(this.idOrdemSelecionada));
    return !!ordem && this.normalizarStatus(ordem.statusAtual) === this.statusPermitido;
  }

  private filtrarOrdensPorStatus(ordens: OrdemServicoResumo[], status: StatusFluxoOrdemServico): OrdemServicoResumo[] {
    return [...(ordens ?? [])].filter(os => this.normalizarStatus(os.statusAtual) === status);
  }

  private normalizarStatus(status?: string): StatusFluxoOrdemServico | '' {
    return (status ?? '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toUpperCase()
      .replace(/\s+/g, '_') as StatusFluxoOrdemServico | '';
  }

  private limparSelecaoOrdem(): void {
    this.idOrdemSelecionada = 0;
    this.itensServico = [];
    this.itensPeca = [];
    this.itemServicoForm = this.itemServicoInicial();
    this.itemPecaForm = this.itemPecaInicial();
  }

  private itemServicoInicial(): ItemServico { return { idOrdemServico: this.idOrdemSelecionada, idServico: 0, idColaborador: 0, quantidade: 1, valorUnitario: 0, descricaoExecucao: '' }; }
  private itemPecaInicial(): ItemPeca { return { idOrdemServico: this.idOrdemSelecionada, idPeca: 0, idFornecedor: 0, quantidade: 1, valorUnitario: 0, observacao: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
