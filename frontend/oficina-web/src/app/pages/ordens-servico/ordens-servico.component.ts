import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { dataHoraFutura } from '../../core/validation/field-validation';
import { ClienteResumo } from '../../models/cliente.model';
import { AlterarStatusOrdemServico, OrdemServicoResumo, PrioridadeOrdemServico, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';
import { VeiculoResumo } from '../../models/veiculo.model';

@Component({ selector: 'app-ordens-servico', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './ordens-servico.component.html' })
export class OrdensServicoComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  clientes: ClienteResumo[] = [];
  veiculos: VeiculoResumo[] = [];
  termo = '';
  termoClienteOs = '';
  termoVeiculoOs = '';
  carregandoClientesOs = false;
  carregandoVeiculosOs = false;
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  prioridades: PrioridadeOrdemServico[] = ['BAIXA', 'NORMAL', 'ALTA', 'URGENTE'];
  statusFluxo: StatusFluxoOrdemServico[] = ['ORCAMENTO', 'EXECUCAO', 'PAGAMENTO', 'FINALIZADO'];
  ordemSelecionada?: OrdemServicoResumo;
  novoStatus: StatusFluxoOrdemServico = 'EXECUCAO';
  observacaoStatus = '';
  form: any = this.formularioInicial();
  errosCampo: Record<string, string> = {};

  constructor(
    private readonly ordemApi: OrdemServicoApiService,
    private readonly clienteApi: ClienteApiService,
    private readonly veiculoApi: VeiculoApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.carregarTelaInicial(); }

  carregarTelaInicial(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    forkJoin({ ordens: this.ordemApi.listar(), clientes: this.clienteApi.listar(), veiculos: this.veiculoApi.listar() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: r => { this.ordens = [...r.ordens]; this.clientes = [...r.clientes]; this.veiculos = [...r.veiculos]; this.atualizarTela(); },
        error: e => { this.erro = e.message; this.atualizarTela(); }
      });
  }

  carregarApoio(): void {
    forkJoin({ clientes: this.clienteApi.listar(), veiculos: this.veiculoApi.listar() }).subscribe({
      next: r => { this.clientes = [...r.clientes]; this.veiculos = [...r.veiculos]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  buscarClientesParaOs(): void {
    const termo = this.termoClienteOs.trim();
    this.carregandoClientesOs = true;
    this.erro = undefined;
    this.atualizarTela();

    const consulta = termo ? this.clienteApi.pesquisar(termo) : this.clienteApi.listar();
    consulta
      .pipe(finalize(() => { this.carregandoClientesOs = false; this.atualizarTela(); }))
      .subscribe({
        next: clientes => { this.clientes = this.mesclarClienteSelecionado(clientes); this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível pesquisar clientes.'; this.atualizarTela(); }
      });
  }

  buscarVeiculosParaOs(): void {
    const termo = this.termoVeiculoOs.trim();
    this.carregandoVeiculosOs = true;
    this.erro = undefined;
    this.atualizarTela();

    const consulta = termo ? this.veiculoApi.pesquisar(termo) : this.veiculoApi.listar();
    consulta
      .pipe(finalize(() => { this.carregandoVeiculosOs = false; this.atualizarTela(); }))
      .subscribe({
        next: veiculos => { this.veiculos = this.mesclarVeiculoSelecionado(veiculos); this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível pesquisar veículos.'; this.atualizarTela(); }
      });
  }

  listar(): void { this.carregando = true; this.erro = undefined; this.atualizarTela(); this.ordemApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: ordens => { this.ordens = [...ordens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  pesquisar(): void { const c = this.termo.trim(); if (!c) { this.listar(); return; } this.carregando = true; this.erro = undefined; this.atualizarTela(); this.ordemApi.pesquisar(c).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: ordens => { this.ordens = [...ordens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

  salvar(): void {
    this.mensagem = undefined; this.erro = undefined;
    if (!this.validarFormulario()) { this.erro = 'Corrija os campos destacados antes de salvar a Ordem de Serviço.'; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.ordemApi.atualizar(this.form.id, this.form) : this.ordemApi.criar(this.form);
    acao.pipe(switchMap(() => this.ordemApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.mensagem = 'Ordem de Serviço salva com sucesso. A tabela foi atualizada automaticamente.'; this.ordens = [...ordens]; this.limpar(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editar(ordem: OrdemServicoResumo): void {
    this.form = { ...ordem };
    this.ordemSelecionada = ordem;
    this.termoClienteOs = ordem.nomeCliente ?? '';
    this.termoVeiculoOs = ordem.placaVeiculo ?? '';
    this.atualizarTela();
  }
  selecionarParaStatus(ordem: OrdemServicoResumo): void {
    this.ordemSelecionada = ordem;
    this.observacaoStatus = '';

    const proximo = this.proximoStatusFluxo(ordem.statusAtual);
    if (proximo) {
      this.novoStatus = proximo;
      this.erro = undefined;
    } else {
      this.erro = this.normalizarStatusFluxo(ordem.statusAtual) === 'PAGAMENTO'
        ? 'A OS está em PAGAMENTO. O registro financeiro deve ser feito somente na tela Pagamentos.'
        : 'Esta Ordem de Serviço já está finalizada ou não possui próxima etapa disponível.';
    }
    this.atualizarTela();
  }

  excluir(ordem: OrdemServicoResumo): void {
    if (!ordem.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.ordemApi.excluir(ordem.id).pipe(switchMap(() => this.ordemApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.mensagem = 'OS inativada. A tabela foi atualizada automaticamente.'; this.ordens = [...ordens]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  alterarStatus(): void {
    if (!this.ordemSelecionada?.id) {
      this.erro = 'Selecione uma Ordem de Serviço.';
      this.atualizarTela();
      return;
    }

    const proximo = this.proximoStatusFluxo(this.ordemSelecionada.statusAtual);
    if (!proximo) {
      this.erro = this.statusAtualNormalizado() === 'PAGAMENTO'
        ? 'A OS está em PAGAMENTO. O registro financeiro deve ser feito somente na tela Pagamentos.'
        : 'Esta Ordem de Serviço já está finalizada ou não possui próxima etapa disponível.';
      this.atualizarTela();
      return;
    }

    this.novoStatus = proximo;
    this.processando = true;
    this.erro = undefined;
    this.atualizarTela();

    const payload: AlterarStatusOrdemServico = {
      novoStatus: proximo,
      observacao: this.observacaoStatus || this.mensagemPadraoAvancoStatus(this.ordemSelecionada.statusAtual, proximo)
    };

    this.ordemApi.alterarStatus(this.ordemSelecionada.id, payload)
      .pipe(
        switchMap(() => this.ordemApi.listar()),
        finalize(() => { this.processando = false; this.atualizarTela(); })
      )
      .subscribe({
        next: ordens => {
          this.mensagem = `OS avançada para ${proximo}. A listagem foi atualizada automaticamente.`;
          this.observacaoStatus = '';
          this.ordens = [...ordens];
          this.ordemSelecionada = undefined;
          this.novoStatus = 'EXECUCAO';
          this.atualizarTela();
        },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  statusAtualNormalizado(): StatusFluxoOrdemServico | '' {
    return this.normalizarStatusFluxo(this.ordemSelecionada?.statusAtual);
  }

  proximoStatusSelecionado(): StatusFluxoOrdemServico | null {
    return this.proximoStatusFluxo(this.ordemSelecionada?.statusAtual);
  }

  textoProximoStatus(): string {
    const proximo = this.proximoStatusSelecionado();
    const statusAtual = this.statusAtualNormalizado();
    if (!this.ordemSelecionada) return 'Selecione uma OS para visualizar a próxima etapa.';
    if (statusAtual === 'PAGAMENTO') {
      return 'Sem avanço nesta tela. A OS está aguardando pagamento.';
    }
    if (statusAtual === 'FINALIZADO') {
      return 'FINALIZADO — a OS já concluiu todo o fluxo.';
    }
    if (!proximo) return 'Esta OS não possui próxima etapa disponível.';
    return `${statusAtual || 'SEM STATUS'} → ${proximo}`;
  }

  podeAvancarStatus(): boolean {
    return !!this.ordemSelecionada?.id && !!this.proximoStatusSelecionado() && !this.processando;
  }

  private proximoStatusFluxo(status?: string): StatusFluxoOrdemServico | null {
    switch (this.normalizarStatusFluxo(status)) {
      case 'ORCAMENTO': return 'EXECUCAO';
      case 'EXECUCAO': return 'PAGAMENTO';
      /*
       * A tela de Ordens de Serviço avança somente até PAGAMENTO.
       * A transição PAGAMENTO -> FINALIZADO pertence ao módulo Pagamentos,
       * onde o backend valida a quitação financeira e finaliza a OS automaticamente.
       */
      case 'PAGAMENTO': return null;
      default: return null;
    }
  }

  private normalizarStatusFluxo(status?: string): StatusFluxoOrdemServico | '' {
    const valor = (status ?? '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toUpperCase()
      .replace(/Ç/g, 'C')
      .trim();

    if (valor === 'ORCAMENTO') return 'ORCAMENTO';
    if (valor === 'EXECUCAO') return 'EXECUCAO';
    if (valor === 'PAGAMENTO') return 'PAGAMENTO';
    if (valor === 'FINALIZADO') return 'FINALIZADO';
    return '';
  }

  private mensagemPadraoAvancoStatus(statusAtual: string | undefined, novoStatus: StatusFluxoOrdemServico): string {
    const atual = this.normalizarStatusFluxo(statusAtual) || 'status anterior';
    return `Avanço operacional do fluxo da OS: ${atual} para ${novoStatus}.`;
  }

  baixarNotaFiscal(ordem: OrdemServicoResumo): void {
    if (!ordem.id) {
      this.erro = 'Selecione uma Ordem de Serviço válida para gerar a nota fiscal.';
      this.atualizarTela();
      return;
    }

    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    this.ordemApi.baixarNotaFiscalPdf(ordem.id)
      .pipe(finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: blob => {
          const nomeArquivo = this.montarNomeArquivoNota(ordem);
          this.salvarArquivo(blob, nomeArquivo);
          this.mensagem = 'Nota fiscal/recibo em PDF gerado com sucesso.';
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message ?? 'Não foi possível gerar a nota fiscal em PDF.';
          this.atualizarTela();
        }
      });
  }

  limpar(): void {
    this.form = this.formularioInicial();
    this.errosCampo = {};
    this.termoClienteOs = '';
    this.termoVeiculoOs = '';
    this.atualizarTela();
  }

  aoAlterarCliente(): void {
    const clienteSelecionado = this.clientes.find(c => Number(c.id) === Number(this.form.idCliente));
    if (clienteSelecionado) {
      this.termoClienteOs = this.rotuloCliente(clienteSelecionado);
    }

    const veiculoSelecionado = this.veiculos.find(v => Number(v.id) === Number(this.form.idVeiculo));
    if (veiculoSelecionado && Number(veiculoSelecionado.proprietarioAtualId ?? 0) !== Number(this.form.idCliente ?? 0)) {
      this.form.idVeiculo = 0;
      this.termoVeiculoOs = '';
    }
    delete this.errosCampo['idCliente'];
    delete this.errosCampo['idVeiculo'];
    this.atualizarTela();
  }

  aoAlterarVeiculo(): void {
    const veiculoSelecionado = this.veiculos.find(v => Number(v.id) === Number(this.form.idVeiculo));
    if (veiculoSelecionado) {
      this.termoVeiculoOs = this.rotuloVeiculo(veiculoSelecionado);
    }
    delete this.errosCampo['idVeiculo'];
    this.atualizarTela();
  }

  rotuloCliente(cliente: ClienteResumo): string {
    return [cliente.nome, cliente.documento].filter(Boolean).join(' — ');
  }

  rotuloVeiculo(veiculo: VeiculoResumo): string {
    const modelo = [veiculo.nomeMarca, veiculo.nomeModelo].filter(Boolean).join(' ');
    return [veiculo.placa, modelo].filter(Boolean).join(' — ');
  }

  veiculosDoClienteSelecionado(): VeiculoResumo[] {
    const idCliente = Number(this.form.idCliente ?? 0);
    if (!idCliente) return this.veiculos;
    return this.veiculos.filter(v => Number(v.proprietarioAtualId ?? 0) === idCliente);
  }

  private mesclarClienteSelecionado(clientes: ClienteResumo[]): ClienteResumo[] {
    const selecionado = this.clientes.find(c => Number(c.id) === Number(this.form.idCliente));
    if (!selecionado || clientes.some(c => Number(c.id) === Number(selecionado.id))) {
      return [...clientes];
    }
    return [selecionado, ...clientes];
  }

  private mesclarVeiculoSelecionado(veiculos: VeiculoResumo[]): VeiculoResumo[] {
    const selecionado = this.veiculos.find(v => Number(v.id) === Number(this.form.idVeiculo));
    if (!selecionado || veiculos.some(v => Number(v.id) === Number(selecionado.id))) {
      return [...veiculos];
    }
    return [selecionado, ...veiculos];
  }

  private validarFormulario(): boolean {
    this.errosCampo = {};
    if (!this.form.idCliente || Number(this.form.idCliente) <= 0) this.errosCampo['idCliente'] = 'Selecione o cliente da Ordem de Serviço.';
    if (!this.form.idVeiculo || Number(this.form.idVeiculo) <= 0) this.errosCampo['idVeiculo'] = 'Selecione o veículo da Ordem de Serviço.';

    const veiculoSelecionado = this.veiculos.find(v => Number(v.id) === Number(this.form.idVeiculo));
    if (veiculoSelecionado && Number(veiculoSelecionado.proprietarioAtualId ?? 0) !== Number(this.form.idCliente ?? 0)) {
      this.errosCampo['idVeiculo'] = 'O veículo selecionado não pertence ao cliente informado como proprietário atual.';
    }

    if (this.form.dataAbertura && dataHoraFutura(this.form.dataAbertura)) this.errosCampo['dataAbertura'] = 'A data de abertura da OS não pode ser futura.';
    if (!this.form.prioridade) this.errosCampo['prioridade'] = 'Selecione a prioridade da OS.';
    return Object.keys(this.errosCampo).length === 0;
  }

  private salvarArquivo(blob: Blob, nomeArquivo: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = nomeArquivo;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  private montarNomeArquivoNota(ordem: OrdemServicoResumo): string {
    const numero = (ordem.numeroOs ?? ordem.id?.toString() ?? 'os')
      .replace(/[^a-zA-Z0-9_-]/g, '-')
      .toLowerCase();
    return `nota-fiscal-${numero}.pdf`;
  }

  private formularioInicial(): any { return { idCliente: 0, idVeiculo: 0, prioridade: 'NORMAL', observacao: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
