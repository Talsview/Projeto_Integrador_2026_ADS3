import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { Colaborador, ColaboradorResumo, Funcao, StatusColaborador } from '../../models/pessoa.model';

@Component({ selector: 'app-colaboradores', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './colaboradores.component.html' })
export class ColaboradoresComponent implements OnInit {
  colaboradores: ColaboradorResumo[] = [];
  funcoes: Funcao[] = [];
  funcoesSelecionadas: Record<number, boolean> = {};
  termo = '';
  carregando = false;
  processando = false;
  mensagem?: string;
  erro?: string;
  statusOptions: StatusColaborador[] = ['ATIVO', 'AFASTADO', 'DESLIGADO'];
  form: Colaborador = this.formularioInicial();

  constructor(
    private readonly colaboradorApi: ColaboradorApiService,
    private readonly funcaoApi: FuncaoApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarTelaInicial();
  }

  carregarTelaInicial(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    forkJoin({
      colaboradores: this.colaboradorApi.listar(),
      funcoes: this.funcaoApi.listar()
    }).pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: resultado => {
        this.colaboradores = [...resultado.colaboradores];
        this.funcoes = [...resultado.funcoes];
        this.atualizarTela();
      },
      error: error => {
        this.erro = error.message ?? 'Não foi possível carregar colaboradores e funções.';
        this.atualizarTela();
      }
    });
  }

  carregarFuncoes(): void {
    this.funcaoApi.listar().subscribe({
      next: funcoes => { this.funcoes = [...funcoes]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.colaboradorApi.listar().pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: colaboradores => { this.colaboradores = [...colaboradores]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) { this.listar(); return; }
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.colaboradorApi.pesquisar(consulta).pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: colaboradores => { this.colaboradores = [...colaboradores]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  isFuncaoSelecionada(id?: number): boolean {
    return id ? Boolean(this.funcoesSelecionadas[id]) : false;
  }

  setFuncaoSelecionada(id: number | undefined, marcado: boolean): void {
    if (!id) return;
    this.funcoesSelecionadas[id] = marcado;
    this.atualizarTela();
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    this.atualizarTela();

    const funcoesIds = Object.entries(this.funcoesSelecionadas).filter(([, marcado]) => marcado).map(([id]) => Number(id));
    const payload: Colaborador = { ...this.form, funcoesIds };
    const acao = payload.id ? this.colaboradorApi.atualizar(payload.id, payload as any) : this.colaboradorApi.criar(payload as any);

    acao.pipe(
      switchMap(() => this.colaboradorApi.listar()),
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: colaboradores => {
        this.mensagem = 'Colaborador salvo com sucesso. A tabela foi atualizada automaticamente.';
        this.colaboradores = [...colaboradores];
        this.limpar();
        this.atualizarTela();
      },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editar(colaborador: ColaboradorResumo): void {
    this.form = {
      id: colaborador.id,
      pessoaId: colaborador.pessoaId,
      nome: colaborador.nome ?? '',
      telefone: colaborador.telefone,
      email: colaborador.email,
      dataAdmissao: colaborador.dataAdmissao,
      statusColaborador: colaborador.statusColaborador ?? 'ATIVO',
      funcoesIds: []
    };
    this.funcoesSelecionadas = {};
    this.atualizarTela();
  }

  excluir(colaborador: ColaboradorResumo): void {
    if (!colaborador.id) return;
    this.processando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.colaboradorApi.excluirEListar(colaborador.id).pipe(
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: colaboradores => {
        this.mensagem = 'Colaborador inativado com sucesso. A tabela foi atualizada automaticamente.';
        this.colaboradores = [...colaboradores];
        this.atualizarTela();
      },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  limpar(): void {
    this.form = this.formularioInicial();
    this.funcoesSelecionadas = {};
    this.atualizarTela();
  }

  private formularioInicial(): Colaborador {
    return { nome: '', telefone: '', email: '', endereco: '', dataAdmissao: '', statusColaborador: 'ATIVO', funcoesIds: [] };
  }

  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
