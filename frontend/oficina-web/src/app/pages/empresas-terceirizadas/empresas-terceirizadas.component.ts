import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { cnpjValido, formatarCnpj, somenteDigitos } from '../../core/validation/documento-validation';
import { emailValido, formatarTelefone, telefoneValido, textoCadastroValido } from '../../core/validation/field-validation';
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
  errosCampo: Record<string, string> = {};

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
    this.mensagem = undefined; this.erro = undefined;
    if (!this.validarFormulario()) { this.erro = 'Corrija os campos destacados antes de salvar a empresa terceirizada.'; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.empresaApi.atualizar(this.form.id, this.form) : this.empresaApi.criar(this.form);
    acao.pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa salva.'; this.empresas = [...empresas]; this.limpar(); this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  editar(empresa: EmpresaTerceirizada): void { this.form = { ...empresa }; this.atualizarTela(); }

  excluir(empresa: EmpresaTerceirizada): void {
    if (!empresa.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.empresaApi.excluir(empresa.id).pipe(switchMap(() => this.empresaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: empresas => { this.mensagem = 'Empresa inativada.'; this.empresas = [...empresas]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.errosCampo = {}; this.atualizarTela(); }
  formatarTelefoneCampo(): void {
    this.form.telefone = formatarTelefone(this.form.telefone);
    this.validarTelefoneSePreenchido();
    this.atualizarTela();
  }

  formatarCnpjCampo(): void {
    this.form.cnpj = formatarCnpj(this.form.cnpj);
    this.validarCnpjSePreenchido();
    this.atualizarTela();
  }

  validarTelefoneSePreenchido(): void {
    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
      return;
    }
    delete this.errosCampo['telefone'];
  }

  validarCnpjSePreenchido(): void {
    const cnpj = somenteDigitos(this.form.cnpj);
    if (!cnpj) {
      delete this.errosCampo['cnpj'];
      return;
    }
    if (cnpj.length < 14) {
      this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
      return;
    }
    if (!cnpjValido(cnpj)) {
      this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real ou deixe o campo vazio.';
      return;
    }
    delete this.errosCampo['cnpj'];
  }

  private validarFormulario(): boolean {
    this.errosCampo = {};
    if (!textoCadastroValido(this.form.nomeEmpresa, true)) this.errosCampo['nomeEmpresa'] = 'Informe um nome de empresa terceirizada válido.';
    const cnpj = somenteDigitos(this.form.cnpj);
    if (cnpj && cnpj.length !== 14) this.errosCampo['cnpj'] = 'O CNPJ deve possuir 14 dígitos.';
    if (cnpj && cnpj.length === 14 && !cnpjValido(cnpj)) this.errosCampo['cnpj'] = 'CNPJ inválido. Informe um CNPJ real ou deixe o campo vazio.';
    if (!telefoneValido(this.form.telefone)) this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
    if (!emailValido(this.form.email)) this.errosCampo['email'] = 'Informe um e-mail válido.';
    return Object.keys(this.errosCampo).length === 0;
  }

  private formularioInicial(): EmpresaTerceirizada { return { nomeEmpresa: '', cnpj: '', telefone: '', email: '', endereco: '' }; }
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
