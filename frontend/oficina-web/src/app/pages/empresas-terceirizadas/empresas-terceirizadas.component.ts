import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { EmpresaTerceirizada } from '../../models/servico.model';

@Component({ selector: 'app-empresas-terceirizadas', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './empresas-terceirizadas.component.html' })
export class EmpresasTerceirizadasComponent implements OnInit {
  empresas: EmpresaTerceirizada[] = [];
  termo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  form: EmpresaTerceirizada = this.formularioInicial();

  constructor(private readonly empresaApi: EmpresaTerceirizadaApiService, private readonly cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.listar(); }

  listar(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: e => { this.empresas = [...e]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisar(): void {
    const t = this.termo.trim();
    if (!t) { this.listar(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: e => { this.empresas = [...e]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  salvar(): void {
    this.mensagem = undefined; this.erro = undefined; this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.empresaApi.atualizar(this.form.id, this.form) : this.empresaApi.criar(this.form);
    acao.pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa terceirizada salva. A tabela foi atualizada automaticamente.'; this.empresas = [...empresas]; this.limpar(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editar(empresa: EmpresaTerceirizada): void { this.form = { ...empresa }; this.atualizarTela(); }

  excluir(empresa: EmpresaTerceirizada): void {
    if (!empresa.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.excluir(empresa.id).pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa inativada. A tabela foi atualizada automaticamente.'; this.empresas = [...empresas]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.atualizarTela(); }
  private formularioInicial(): EmpresaTerceirizada { return { nomeEmpresa: '', cnpj: '', telefone: '', email: '', endereco: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
