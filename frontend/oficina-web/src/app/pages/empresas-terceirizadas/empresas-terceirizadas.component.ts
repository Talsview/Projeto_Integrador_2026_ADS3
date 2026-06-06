import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
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
  form: EmpresaTerceirizada = { nomeEmpresa: '', cnpj: '', telefone: '', email: '', endereco: '' };
  constructor(private readonly empresaApi: EmpresaTerceirizadaApiService) {}
  ngOnInit(): void { this.listar(); }
  listar(): void { this.carregando = true; this.empresaApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: e => this.empresas = e, error: error => this.erro = error.message }); }
  pesquisar(): void { const t = this.termo.trim(); if (!t) { this.listar(); return; } this.carregando = true; this.empresaApi.pesquisar(t).pipe(finalize(() => this.carregando = false)).subscribe({ next: e => this.empresas = e, error: error => this.erro = error.message }); }
  salvar(): void { this.processando = true; const a = this.form.id ? this.empresaApi.atualizar(this.form.id, this.form) : this.empresaApi.criar(this.form); a.pipe(finalize(() => this.processando = false)).subscribe({ next: empresa => { this.mensagem = 'Empresa terceirizada salva.'; this.inserirOuAtualizar(empresa); this.limpar(); this.listar(); }, error: e => this.erro = e.message }); }
  editar(empresa: EmpresaTerceirizada): void { this.form = { ...empresa }; }
  excluir(empresa: EmpresaTerceirizada): void { if (!empresa.id) return; this.processando = true; this.empresaApi.excluir(empresa.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'Empresa inativada.'; this.empresas = this.empresas.filter(item => item.id !== empresa.id); this.listar(); }, error: e => this.erro = e.message }); }
  limpar(): void { this.form = { nomeEmpresa: '', cnpj: '', telefone: '', email: '', endereco: '' }; }
  private inserirOuAtualizar(empresa: EmpresaTerceirizada | null | undefined): void { if (!empresa?.id) return; const existe = this.empresas.some(item => item.id === empresa.id); this.empresas = existe ? this.empresas.map(item => item.id === empresa.id ? empresa : item) : [empresa, ...this.empresas]; }
}
