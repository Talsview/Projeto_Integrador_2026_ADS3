import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { ClienteResumo } from '../../models/cliente.model';
import { AlterarStatusOrdemServico, OrdemServicoResumo, PrioridadeOrdemServico, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';
import { VeiculoResumo } from '../../models/veiculo.model';

@Component({ selector: 'app-ordens-servico', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './ordens-servico.component.html' })
export class OrdensServicoComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  clientes: ClienteResumo[] = [];
  veiculos: VeiculoResumo[] = [];
  termo = '';
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

  listar(): void { this.carregando = true; this.erro = undefined; this.atualizarTela(); this.ordemApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: ordens => { this.ordens = [...ordens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  pesquisar(): void { const c = this.termo.trim(); if (!c) { this.listar(); return; } this.carregando = true; this.erro = undefined; this.atualizarTela(); this.ordemApi.pesquisar(c).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: ordens => { this.ordens = [...ordens]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

  salvar(): void {
    this.mensagem = undefined; this.erro = undefined; this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.ordemApi.atualizar(this.form.id, this.form) : this.ordemApi.criar(this.form);
    acao.pipe(switchMap(() => this.ordemApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.mensagem = 'Ordem de Serviço salva com sucesso. A tabela foi atualizada automaticamente.'; this.ordens = [...ordens]; this.limpar(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editar(ordem: OrdemServicoResumo): void { this.form = { ...ordem }; this.ordemSelecionada = ordem; this.atualizarTela(); }
  selecionarParaStatus(ordem: OrdemServicoResumo): void { this.ordemSelecionada = ordem; this.observacaoStatus = ''; this.atualizarTela(); }

  excluir(ordem: OrdemServicoResumo): void {
    if (!ordem.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.ordemApi.excluir(ordem.id).pipe(switchMap(() => this.ordemApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.mensagem = 'OS inativada. A tabela foi atualizada automaticamente.'; this.ordens = [...ordens]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  alterarStatus(): void {
    if (!this.ordemSelecionada?.id) { this.erro = 'Selecione uma Ordem de Serviço.'; this.atualizarTela(); return; }
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const payload: AlterarStatusOrdemServico = { novoStatus: this.novoStatus, observacao: this.observacaoStatus };
    this.ordemApi.alterarStatus(this.ordemSelecionada.id, payload).pipe(switchMap(() => this.ordemApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.mensagem = 'Status alterado com sucesso. A tabela foi atualizada automaticamente.'; this.observacaoStatus = ''; this.ordens = [...ordens]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.atualizarTela(); }
  private formularioInicial(): any { return { idCliente: 0, idVeiculo: 0, prioridade: 'NORMAL', observacao: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
