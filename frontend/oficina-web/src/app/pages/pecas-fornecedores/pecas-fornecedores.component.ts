import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { FornecedorApiService } from '../../core/services/fornecedor-api.service';
import { PecaApiService } from '../../core/services/peca-api.service';
import { cnpjValido, somenteDigitos } from '../../core/validation/documento-validation';
import { anoVeiculoValido, emailValido, numeroNaoNegativo, telefoneValido, textoCadastroValido } from '../../core/validation/field-validation';
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
  fornecedorForm: Fornecedor = this.fornecedorInicial();
  pecaForm: Peca = this.pecaInicial();

  constructor(private readonly fornecedorApi: FornecedorApiService, private readonly pecaApi: PecaApiService, private readonly cdr: ChangeDetectorRef) {}
  ngOnInit(): void { this.listarTudo(); }

  listarTudo(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    forkJoin({ fornecedores: this.fornecedorApi.listar(), pecas: this.pecaApi.listar() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: r => { this.fornecedores = [...r.fornecedores]; this.pecas = [...r.pecas]; this.atualizarTela(); },
        error: e => { this.erro = e.message; this.atualizarTela(); }
      });
  }

  listarFornecedores(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.fornecedorApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: f => { this.fornecedores = [...f]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } });
  }

  listarPecas(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.pecaApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: p => { this.pecas = [...p]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } });
  }

  pesquisarFornecedores(): void { const t = this.termoFornecedor.trim(); if (!t) { this.listarFornecedores(); return; } this.carregando = true; this.erro = undefined; this.atualizarTela(); this.fornecedorApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: f => { this.fornecedores = [...f]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  pesquisarPecas(): void { const t = this.termoPeca.trim(); if (!t) { this.listarPecas(); return; } this.carregando = true; this.erro = undefined; this.atualizarTela(); this.pecaApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({ next: p => { this.pecas = [...p]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

  salvarFornecedor(): void {
    this.mensagem = undefined; this.erro = undefined;
    const erroValidacao = this.validarFornecedor();
    if (erroValidacao) { this.erro = erroValidacao; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.fornecedorForm.id ? this.fornecedorApi.atualizar(this.fornecedorForm.id, this.fornecedorForm) : this.fornecedorApi.criar(this.fornecedorForm);
    acao.pipe(switchMap(() => this.fornecedorApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: fornecedores => { this.mensagem = 'Fornecedor salvo. A lista foi atualizada automaticamente.'; this.fornecedores = [...fornecedores]; this.fornecedorForm = this.fornecedorInicial(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  salvarPeca(): void {
    this.mensagem = undefined; this.erro = undefined;
    const erroValidacao = this.validarPeca();
    if (erroValidacao) { this.erro = erroValidacao; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.pecaForm.id ? this.pecaApi.atualizar(this.pecaForm.id, this.pecaForm) : this.pecaApi.criar(this.pecaForm);
    acao.pipe(switchMap(() => this.pecaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: pecas => { this.mensagem = 'Peça salva. A lista foi atualizada automaticamente.'; this.pecas = [...pecas]; this.pecaForm = this.pecaInicial(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editarFornecedor(f: Fornecedor): void { this.fornecedorForm = { ...f }; this.atualizarTela(); }
  editarPeca(p: Peca): void { this.pecaForm = { ...p }; this.atualizarTela(); }

  excluirFornecedor(f: Fornecedor): void { if (!f.id) return; this.processando = true; this.erro = undefined; this.atualizarTela(); this.fornecedorApi.excluir(f.id).pipe(switchMap(() => this.fornecedorApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: fornecedores => { this.mensagem = 'Fornecedor inativado. A lista foi atualizada automaticamente.'; this.fornecedores = [...fornecedores]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }
  excluirPeca(p: Peca): void { if (!p.id) return; this.processando = true; this.erro = undefined; this.atualizarTela(); this.pecaApi.excluir(p.id).pipe(switchMap(() => this.pecaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({ next: pecas => { this.mensagem = 'Peça inativada. A lista foi atualizada automaticamente.'; this.pecas = [...pecas]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } }); }

  private validarFornecedor(): string | undefined {
    if (!textoCadastroValido(this.fornecedorForm.nomeFornecedor, true)) return 'Informe um nome de fornecedor válido.';
    const cnpj = somenteDigitos(this.fornecedorForm.cnpj);
    if (cnpj && !cnpjValido(cnpj)) return 'Informe um CNPJ de fornecedor válido ou deixe o campo vazio.';
    if (!telefoneValido(this.fornecedorForm.telefone)) return 'Informe um telefone válido com DDD.';
    if (!emailValido(this.fornecedorForm.email)) return 'Informe um e-mail válido.';
    return undefined;
  }

  private validarPeca(): string | undefined {
    if (!textoCadastroValido(this.pecaForm.nomePeca, true)) return 'Informe um nome de peça válido.';
    if (this.pecaForm.marcaPeca && !textoCadastroValido(this.pecaForm.marcaPeca, false)) return 'A marca da peça contém caracteres inválidos.';
    if (this.pecaForm.modeloAplicavel && !textoCadastroValido(this.pecaForm.modeloAplicavel, false)) return 'O modelo aplicável contém caracteres inválidos.';
    if (!anoVeiculoValido(this.pecaForm.anoVeiculo, false)) return 'Informe um ano de veículo válido para a peça.';
    if (!anoVeiculoValido(this.pecaForm.anoModelo, false)) return 'Informe um ano modelo válido para a peça.';
    if (this.pecaForm.anoVeiculo && this.pecaForm.anoModelo && (Number(this.pecaForm.anoModelo) < Number(this.pecaForm.anoVeiculo) - 1 || Number(this.pecaForm.anoModelo) > Number(this.pecaForm.anoVeiculo) + 1)) return 'O ano modelo da peça deve ser coerente com o ano do veículo.';
    if (!numeroNaoNegativo(this.pecaForm.prazoGarantiaDias)) return 'O prazo de garantia da peça não pode ser negativo.';
    return undefined;
  }

  private fornecedorInicial(): Fornecedor { return { nomeFornecedor: '', cnpj: '', telefone: '', email: '', endereco: '' }; }
  private pecaInicial(): Peca { return { nomePeca: '', codigoNacional: '', marcaPeca: '', modeloAplicavel: '', anoVeiculo: undefined, anoModelo: undefined, prazoGarantiaDias: 90, descricao: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
