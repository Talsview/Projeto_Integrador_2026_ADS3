import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
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
  ordemSelecionada?: OrdemServicoResumo;
  novoStatus: StatusFluxoOrdemServico = 'EXECUCAO';
  observacaoStatus = '';
  erro?: string;
  mensagem?: string;
  carregando = false;
  processando = false;
  prioridades: PrioridadeOrdemServico[] = ['BAIXA', 'NORMAL', 'ALTA', 'URGENTE'];
  statusFluxo: StatusFluxoOrdemServico[] = ['ORCAMENTO', 'EXECUCAO', 'PAGAMENTO', 'FINALIZADO'];

  form: any = { idCliente: 0, idVeiculo: 0, numeroOs: '', prioridade: 'NORMAL', observacao: '' };

  constructor(
    private readonly ordemApi: OrdemServicoApiService,
    private readonly clienteApi: ClienteApiService,
    private readonly veiculoApi: VeiculoApiService
  ) {}

  ngOnInit(): void { this.listar(); this.carregarApoio(); }
  carregarApoio(): void { this.clienteApi.listar().subscribe({ next: c => this.clientes = c }); this.veiculoApi.listar().subscribe({ next: v => this.veiculos = v }); }
  listar(): void { this.carregando = true; this.erro = undefined; this.ordemApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: ordens => this.ordens = ordens, error: error => this.erro = error.message }); }
  pesquisar(): void { const c = this.termo.trim(); if (!c) { this.listar(); return; } this.carregando = true; this.erro = undefined; this.ordemApi.pesquisar(c).pipe(finalize(() => this.carregando = false)).subscribe({ next: ordens => this.ordens = ordens, error: error => this.erro = error.message }); }
  salvar(): void { this.mensagem = undefined; this.erro = undefined; this.processando = true; const a = this.form.id ? this.ordemApi.atualizar(this.form.id, this.form) : this.ordemApi.criar(this.form); a.pipe(finalize(() => this.processando = false)).subscribe({ next: ordem => { this.mensagem = 'Ordem de serviço salva com sucesso.'; this.inserirOuAtualizar(ordem as OrdemServicoResumo); this.limpar(); this.listar(); }, error: e => this.erro = e.message }); }
  editar(ordem: OrdemServicoResumo): void { this.form = { ...ordem, idCliente: ordem.idCliente ?? 0, idVeiculo: ordem.idVeiculo ?? 0 }; }
  selecionar(ordem: OrdemServicoResumo): void { this.ordemSelecionada = ordem; this.mensagem = undefined; this.erro = undefined; }
  excluir(ordem: OrdemServicoResumo): void { if (!ordem.id) return; this.processando = true; this.ordemApi.excluir(ordem.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'OS inativada.'; this.ordens = this.ordens.filter(item => item.id !== ordem.id); this.listar(); }, error: e => this.erro = e.message }); }
  limpar(): void { this.form = { idCliente: 0, idVeiculo: 0, numeroOs: '', prioridade: 'NORMAL', observacao: '' }; }
  alterarStatus(): void {
    if (!this.ordemSelecionada?.id) { this.erro = 'Selecione uma ordem de serviço.'; return; }
    this.processando = true;
    const payload: AlterarStatusOrdemServico = { novoStatus: this.novoStatus, observacao: this.observacaoStatus };
    this.ordemApi.alterarStatus(this.ordemSelecionada.id, payload).pipe(finalize(() => this.processando = false)).subscribe({ next: ordem => { this.mensagem = 'Status alterado com sucesso.'; this.observacaoStatus = ''; this.inserirOuAtualizar(ordem as OrdemServicoResumo); this.listar(); }, error: error => this.erro = error.message });
  }

  private inserirOuAtualizar(ordem: OrdemServicoResumo | null | undefined): void {
    if (!ordem?.id) return;
    const existe = this.ordens.some(item => item.id === ordem.id);
    this.ordens = existe ? this.ordens.map(item => item.id === ordem.id ? ordem : item) : [ordem, ...this.ordens];
  }
}
