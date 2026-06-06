import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { Colaborador, ColaboradorResumo, Funcao, StatusColaborador } from '../../models/pessoa.model';

@Component({
  selector: 'app-colaboradores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './colaboradores.component.html'
})
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
  form: Colaborador = { nome: '', telefone: '', email: '', endereco: '', dataAdmissao: '', statusColaborador: 'ATIVO', funcoesIds: [] };

  constructor(
    private readonly colaboradorApi: ColaboradorApiService,
    private readonly funcaoApi: FuncaoApiService
  ) {}

  ngOnInit(): void {
    this.listar();
    this.carregarFuncoes();
  }

  carregarFuncoes(): void {
    this.funcaoApi.listar().subscribe({ next: funcoes => this.funcoes = funcoes, error: error => this.erro = error.message });
  }

  listar(): void {
    this.carregando = true;
    this.erro = undefined;
    this.colaboradorApi.listar()
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: colaboradores => this.colaboradores = colaboradores,
        error: error => this.erro = error.message
      });
  }

  pesquisar(): void {
    const consulta = this.termo.trim();
    if (!consulta) { this.listar(); return; }
    this.carregando = true;
    this.erro = undefined;
    this.colaboradorApi.pesquisar(consulta)
      .pipe(finalize(() => this.carregando = false))
      .subscribe({
        next: colaboradores => this.colaboradores = colaboradores,
        error: error => this.erro = error.message
      });
  }

  isFuncaoSelecionada(id?: number): boolean {
    return id ? Boolean(this.funcoesSelecionadas[id]) : false;
  }

  setFuncaoSelecionada(id: number | undefined, marcado: boolean): void {
    if (!id) return;
    this.funcoesSelecionadas[id] = marcado;
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    const funcoesIds = Object.entries(this.funcoesSelecionadas).filter(([, marcado]) => marcado).map(([id]) => Number(id));
    const payload: Colaborador = { ...this.form, funcoesIds };

    const acao = payload.id ? this.colaboradorApi.atualizar(payload.id, payload as any) : this.colaboradorApi.criar(payload as any);
    acao.pipe(finalize(() => this.processando = false)).subscribe({
      next: colaborador => {
        this.mensagem = 'Colaborador salvo com sucesso.';
        this.inserirOuAtualizar(colaborador as ColaboradorResumo);
        this.limpar();
        this.listar();
      },
      error: error => this.erro = error.message
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
  }

  excluir(colaborador: ColaboradorResumo): void {
    if (!colaborador.id) return;
    this.processando = true;
    this.colaboradorApi.excluir(colaborador.id)
      .pipe(finalize(() => this.processando = false))
      .subscribe({
        next: () => {
          this.mensagem = 'Colaborador inativado com sucesso.';
          this.colaboradores = this.colaboradores.filter(item => item.id !== colaborador.id);
          this.listar();
        },
        error: error => this.erro = error.message
      });
  }

  limpar(): void {
    this.form = { nome: '', telefone: '', email: '', endereco: '', dataAdmissao: '', statusColaborador: 'ATIVO', funcoesIds: [] };
    this.funcoesSelecionadas = {};
  }

  private inserirOuAtualizar(colaborador: ColaboradorResumo | null | undefined): void {
    if (!colaborador?.id) return;
    const existe = this.colaboradores.some(item => item.id === colaborador.id);
    this.colaboradores = existe
      ? this.colaboradores.map(item => item.id === colaborador.id ? colaborador : item)
      : [colaborador, ...this.colaboradores];
  }
}
