import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { ServicoApiService } from '../../core/services/servico-api.service';
import { Servico, TipoServico } from '../../models/servico.model';

@Component({ selector: 'app-servicos', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './servicos.component.html' })
export class ServicosComponent implements OnInit {
  servicos: Servico[] = [];
  termo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  tipos: TipoServico[] = ['INTERNO', 'TERCEIRIZADO'];
  form: Servico = { nomeServico: '', descricao: '', prazoGarantiaDias: 90, valorBase: 0, tipoServico: 'INTERNO', observacaoInterna: '', observacaoTerceirizacao: '' };

  constructor(private readonly servicoApi: ServicoApiService) {}
  ngOnInit(): void { this.listar(); }

  listar(): void { this.carregando = true; this.erro = undefined; this.servicoApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: servicos => this.servicos = servicos, error: error => this.erro = error.message }); }
  pesquisar(): void { const t = this.termo.trim(); if (!t) { this.listar(); return; } this.carregando = true; this.erro = undefined; this.servicoApi.pesquisar(t).pipe(finalize(() => this.carregando = false)).subscribe({ next: s => this.servicos = s, error: e => this.erro = e.message }); }
  salvar(): void { this.mensagem = undefined; this.erro = undefined; this.processando = true; const acao = this.form.id ? this.servicoApi.atualizar(this.form.id, this.form) : this.servicoApi.criar(this.form); acao.pipe(finalize(() => this.processando = false)).subscribe({ next: servico => { this.mensagem = 'Serviço salvo com sucesso.'; this.inserirOuAtualizar(servico); this.limpar(); this.listar(); }, error: error => this.erro = error.message }); }
  editar(servico: Servico): void { this.form = { ...servico }; }
  excluir(servico: Servico): void { if (!servico.id) return; this.processando = true; this.servicoApi.excluir(servico.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'Serviço inativado.'; this.servicos = this.servicos.filter(item => item.id !== servico.id); this.listar(); }, error: e => this.erro = e.message }); }
  limpar(): void { this.form = { nomeServico: '', descricao: '', prazoGarantiaDias: 90, valorBase: 0, tipoServico: 'INTERNO', observacaoInterna: '', observacaoTerceirizacao: '' }; }

  private inserirOuAtualizar(servico: Servico | null | undefined): void {
    if (!servico?.id) return;
    const existe = this.servicos.some(item => item.id === servico.id);
    this.servicos = existe ? this.servicos.map(item => item.id === servico.id ? servico : item) : [servico, ...this.servicos];
  }
}
