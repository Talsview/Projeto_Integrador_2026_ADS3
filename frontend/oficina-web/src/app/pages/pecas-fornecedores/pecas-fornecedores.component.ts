import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { FornecedorApiService } from '../../core/services/fornecedor-api.service';
import { PecaApiService } from '../../core/services/peca-api.service';
import { Fornecedor, Peca } from '../../models/peca.model';

@Component({ selector: 'app-pecas-fornecedores', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './pecas-fornecedores.component.html' })
export class PecasFornecedoresComponent implements OnInit {
  fornecedores: Fornecedor[] = [];
  pecas: Peca[] = [];
  termoFornecedor = '';
  termoPeca = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  fornecedorForm: Fornecedor = { nomeFornecedor: '', cnpj: '', telefone: '', email: '', endereco: '' };
  pecaForm: Peca = { nomePeca: '', codigoNacional: '', marcaPeca: '', modeloAplicavel: '', anoVeiculo: undefined, anoModelo: undefined, prazoGarantiaDias: 90, descricao: '' };

  constructor(private readonly fornecedorApi: FornecedorApiService, private readonly pecaApi: PecaApiService) {}
  ngOnInit(): void { this.listarTudo(); }
  listarTudo(): void { this.listarFornecedores(); this.listarPecas(); }
  listarFornecedores(): void { this.carregando = true; this.fornecedorApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: f => this.fornecedores = f, error: e => this.erro = e.message }); }
  listarPecas(): void { this.carregando = true; this.pecaApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: p => this.pecas = p, error: e => this.erro = e.message }); }
  pesquisarFornecedores(): void { const t = this.termoFornecedor.trim(); if (!t) { this.listarFornecedores(); return; } this.carregando = true; this.fornecedorApi.pesquisar(t).pipe(finalize(() => this.carregando = false)).subscribe({ next: f => this.fornecedores = f, error: e => this.erro = e.message }); }
  pesquisarPecas(): void { const t = this.termoPeca.trim(); if (!t) { this.listarPecas(); return; } this.carregando = true; this.pecaApi.pesquisar(t).pipe(finalize(() => this.carregando = false)).subscribe({ next: p => this.pecas = p, error: e => this.erro = e.message }); }
  salvarFornecedor(): void { this.processando = true; const a = this.fornecedorForm.id ? this.fornecedorApi.atualizar(this.fornecedorForm.id, this.fornecedorForm) : this.fornecedorApi.criar(this.fornecedorForm); a.pipe(finalize(() => this.processando = false)).subscribe({ next: fornecedor => { this.mensagem = 'Fornecedor salvo.'; this.inserirOuAtualizarFornecedor(fornecedor); this.fornecedorForm = { nomeFornecedor: '', cnpj: '', telefone: '', email: '', endereco: '' }; this.listarFornecedores(); }, error: e => this.erro = e.message }); }
  salvarPeca(): void { this.processando = true; const a = this.pecaForm.id ? this.pecaApi.atualizar(this.pecaForm.id, this.pecaForm) : this.pecaApi.criar(this.pecaForm); a.pipe(finalize(() => this.processando = false)).subscribe({ next: peca => { this.mensagem = 'Peça salva.'; this.inserirOuAtualizarPeca(peca); this.pecaForm = { nomePeca: '', codigoNacional: '', marcaPeca: '', modeloAplicavel: '', prazoGarantiaDias: 90, descricao: '' }; this.listarPecas(); }, error: e => this.erro = e.message }); }
  editarFornecedor(f: Fornecedor): void { this.fornecedorForm = { ...f }; }
  editarPeca(p: Peca): void { this.pecaForm = { ...p }; }
  excluirFornecedor(f: Fornecedor): void { if (!f.id) return; this.processando = true; this.fornecedorApi.excluir(f.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'Fornecedor inativado.'; this.fornecedores = this.fornecedores.filter(item => item.id !== f.id); this.listarFornecedores(); }, error: e => this.erro = e.message }); }
  excluirPeca(p: Peca): void { if (!p.id) return; this.processando = true; this.pecaApi.excluir(p.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'Peça inativada.'; this.pecas = this.pecas.filter(item => item.id !== p.id); this.listarPecas(); }, error: e => this.erro = e.message }); }
  private inserirOuAtualizarFornecedor(fornecedor: Fornecedor | null | undefined): void { if (!fornecedor?.id) return; const existe = this.fornecedores.some(item => item.id === fornecedor.id); this.fornecedores = existe ? this.fornecedores.map(item => item.id === fornecedor.id ? fornecedor : item) : [fornecedor, ...this.fornecedores]; }
  private inserirOuAtualizarPeca(peca: Peca | null | undefined): void { if (!peca?.id) return; const existe = this.pecas.some(item => item.id === peca.id); this.pecas = existe ? this.pecas.map(item => item.id === peca.id ? peca : item) : [peca, ...this.pecas]; }
}
