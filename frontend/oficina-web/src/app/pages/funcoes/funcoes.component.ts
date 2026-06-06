import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
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
  sincronizandoTabela = false;
  mensagem?: string;
  erro?: string;

  form: Funcao = { nomeFuncao: '', descricao: '' };

  constructor(
    private readonly funcaoApi: FuncaoApiService,
    private readonly changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.listar(); }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.funcaoApi.listar()
      .pipe(finalize(() => {
        this.carregando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: funcoes => {
          this.funcoes = [...funcoes];
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message;
          this.atualizarTela();
        }
      });
  }

  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) { this.listar(); return; }
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.funcaoApi.pesquisar(consulta)
      .pipe(finalize(() => {
        this.carregando = false;
        this.atualizarTela();
      }))
      .subscribe({
        next: funcoes => {
          this.funcoes = [...funcoes];
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message;
          this.atualizarTela();
        }
      });
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    this.sincronizandoTabela = true;
    this.atualizarTela();

    const acao = this.form.id ? this.funcaoApi.atualizar(this.form.id, this.form) : this.funcaoApi.criar(this.form);
    acao.pipe(
      switchMap(() => this.funcaoApi.listar()),
      finalize(() => {
        this.processando = false;
        this.sincronizandoTabela = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: funcoesAtualizadas => {
        this.funcoes = [...funcoesAtualizadas];
        this.mensagem = 'Função salva com sucesso. A tabela foi atualizada automaticamente.';
        this.limpar();
        this.atualizarTela();
      },
      error: error => {
        this.erro = error.message;
        this.atualizarTela();
      }
    });
  }

  editar(funcao: Funcao): void {
    this.form = { ...funcao };
    this.atualizarTela();
  }

  excluir(funcao: Funcao): void {
    if (!funcao.id) return;
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    this.sincronizandoTabela = true;
    this.atualizarTela();

    this.funcaoApi.excluir(funcao.id)
      .pipe(
        switchMap(() => this.funcaoApi.listar()),
        finalize(() => {
          this.processando = false;
          this.sincronizandoTabela = false;
          this.atualizarTela();
        })
      )
      .subscribe({
        next: funcoesAtualizadas => {
          this.funcoes = [...funcoesAtualizadas];
          this.mensagem = 'Função inativada com sucesso. A tabela foi atualizada automaticamente.';
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message;
          this.atualizarTela();
        }
      });
  }

  limpar(): void {
    this.form = { nomeFuncao: '', descricao: '' };
    this.atualizarTela();
  }

  private atualizarTela(): void {
    this.changeDetector.detectChanges();
  }
}
