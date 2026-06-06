import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { Funcao } from '../../models/pessoa.model';

@Component({
  selector: 'app-funcoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './funcoes.component.html'
})
export class FuncoesComponent implements OnInit {
  funcoes: Funcao[] = [];
  termo = '';
  carregando = false;
  processando = false;
  mensagem?: string;
  erro?: string;

  form: Funcao = { nomeFuncao: '', descricao: '' };

  constructor(private readonly funcaoApi: FuncaoApiService) {}

  ngOnInit(): void { this.listar(); }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.funcaoApi.listar()
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: funcoes => this.funcoes = funcoes,
        error: error => this.erro = error.message
      });
  }

  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) { this.listar(); return; }
    this.carregando = true;
    this.erro = undefined;
    this.funcaoApi.pesquisar(consulta)
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: funcoes => this.funcoes = funcoes,
        error: error => this.erro = error.message
      });
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    const acao = this.form.id ? this.funcaoApi.atualizar(this.form.id, this.form) : this.funcaoApi.criar(this.form);
    acao.pipe(finalize(() => this.processando = false)).subscribe({
      next: funcao => {
        this.mensagem = 'Função salva com sucesso.';
        this.inserirOuAtualizar(funcao);
        this.limpar();
        this.listar();
      },
      error: error => this.erro = error.message
    });
  }

  editar(funcao: Funcao): void { this.form = { ...funcao }; }

  excluir(funcao: Funcao): void {
    if (!funcao.id) return;
    this.processando = true;
    this.funcaoApi.excluir(funcao.id)
      .pipe(finalize(() => this.processando = false))
      .subscribe({
        next: () => {
          this.mensagem = 'Função inativada com sucesso.';
          this.funcoes = this.funcoes.filter(item => item.id !== funcao.id);
          this.listar();
        },
        error: error => this.erro = error.message
      });
  }

  limpar(): void { this.form = { nomeFuncao: '', descricao: '' }; }

  private inserirOuAtualizar(funcao: Funcao | null | undefined): void {
    if (!funcao?.id) return;
    const existe = this.funcoes.some(item => item.id === funcao.id);
    this.funcoes = existe
      ? this.funcoes.map(item => item.id === funcao.id ? funcao : item)
      : [funcao, ...this.funcoes];
  }
}
