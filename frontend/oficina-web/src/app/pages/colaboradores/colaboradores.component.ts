import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { dataFutura, emailValido, formatarTelefone, nomePessoaValido, telefoneValido } from '../../core/validation/field-validation';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { Colaborador, ColaboradorFuncao, ColaboradorResumo, Funcao, StatusColaborador } from '../../models/pessoa.model';

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
  errosCampo: Record<string, string> = {};
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
    this.validarFuncoesSelecionadas();
    this.atualizarTela();
  }

  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;

    if (!this.validarFormulario()) {
      this.erro = 'Corrija os campos destacados antes de salvar o colaborador.';
      this.atualizarTela();
      return;
    }

    this.processando = true;
    this.atualizarTela();

    const funcoesIds = this.obterFuncoesSelecionadas();
    const payload: Colaborador = {
      ...this.form,
      nome: this.form.nome?.trim(),
      telefone: this.form.telefone?.trim(),
      email: this.form.email?.trim(),
      endereco: this.form.endereco?.trim(),
      funcoesIds
    };
    const acao = payload.id ? this.colaboradorApi.atualizar(payload.id, payload as any) : this.colaboradorApi.criar(payload as any);

    acao.pipe(
      switchMap(() => this.colaboradorApi.listar()),
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: colaboradores => {
        this.colaboradores = [...colaboradores];
        const editando = Boolean(this.form.id);
        this.limpar(false);
        this.mensagem = editando
          ? 'Colaborador atualizado.'
          : 'Colaborador cadastrado.';
        this.atualizarTela();
      },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editar(colaborador: ColaboradorResumo): void {
    if (!colaborador.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.errosCampo = {};
    this.atualizarTela();

    this.colaboradorApi.buscarPorId(colaborador.id).pipe(finalize(() => {
      this.processando = false;
      this.atualizarTela();
    })).subscribe({
      next: detalhe => {
        const colaboradorDetalhado = detalhe as unknown as Colaborador;
        this.form = {
          id: colaboradorDetalhado.id ?? colaborador.id,
          pessoaId: colaboradorDetalhado.pessoaId ?? colaborador.pessoaId,
          nome: colaboradorDetalhado.nome ?? colaborador.nome ?? '',
          telefone: formatarTelefone(colaboradorDetalhado.telefone ?? colaborador.telefone ?? ''),
          email: colaboradorDetalhado.email ?? colaborador.email ?? '',
          endereco: colaboradorDetalhado.endereco ?? '',
          dataAdmissao: colaboradorDetalhado.dataAdmissao ?? colaborador.dataAdmissao ?? '',
          statusColaborador: colaboradorDetalhado.statusColaborador ?? colaborador.statusColaborador ?? 'ATIVO',
          funcoesIds: []
        };
        this.funcoesSelecionadas = {};
        const funcoesVinculadas = colaboradorDetalhado.funcoes ?? [];
        funcoesVinculadas.forEach((funcao: ColaboradorFuncao) => {
          if (funcao.funcaoId) {
            this.funcoesSelecionadas[funcao.funcaoId] = true;
          }
        });
        this.atualizarTela();
      },
      error: error => { this.erro = error.message ?? 'Não foi possível carregar o colaborador para edição.'; this.atualizarTela(); }
    });
  }

  excluir(colaborador: ColaboradorResumo): void {
    if (!colaborador.id) return;
    const confirmou = window.confirm(`Deseja realmente inativar o colaborador ${colaborador.nome ?? ''}?`);
    if (!confirmou) return;

    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    this.colaboradorApi.excluirEListar(colaborador.id).pipe(
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: colaboradores => {
        this.mensagem = 'Colaborador inativado.';
        this.colaboradores = [...colaboradores];
        this.atualizarTela();
      },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  limpar(limparMensagens = true): void {
    this.form = this.formularioInicial();
    this.funcoesSelecionadas = {};
    this.errosCampo = {};
    if (limparMensagens) {
      this.mensagem = undefined;
      this.erro = undefined;
    }
    this.atualizarTela();
  }

  formatarTelefoneCampo(): void {
    this.form.telefone = formatarTelefone(this.form.telefone);
    this.validarTelefoneSePreenchido();
    this.atualizarTela();
  }

  validarNome(): void {
    if (!nomePessoaValido(this.form.nome)) {
      this.errosCampo['nome'] = 'Informe um nome válido, sem números ou caracteres especiais indevidos.';
      return;
    }
    delete this.errosCampo['nome'];
  }

  validarTelefoneSePreenchido(): void {
    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
      return;
    }
    delete this.errosCampo['telefone'];
  }

  validarEmailSePreenchido(): void {
    if (!emailValido(this.form.email)) {
      this.errosCampo['email'] = 'Informe um e-mail válido.';
      return;
    }
    delete this.errosCampo['email'];
  }

  validarDataAdmissao(): void {
    if (dataFutura(this.form.dataAdmissao)) {
      this.errosCampo['dataAdmissao'] = 'A data de admissão não pode ser futura.';
      return;
    }
    delete this.errosCampo['dataAdmissao'];
  }

  validarFuncoesSelecionadas(): void {
    if (this.obterFuncoesSelecionadas().length === 0) {
      this.errosCampo['funcoes'] = 'Selecione pelo menos uma função para o colaborador.';
      return;
    }
    delete this.errosCampo['funcoes'];
  }

  private validarFormulario(): boolean {
    this.errosCampo = {};
    this.validarNome();
    this.validarTelefoneSePreenchido();
    this.validarEmailSePreenchido();
    this.validarDataAdmissao();
    this.validarFuncoesSelecionadas();
    return Object.keys(this.errosCampo).length === 0;
  }

  private obterFuncoesSelecionadas(): number[] {
    return Object.entries(this.funcoesSelecionadas)
      .filter(([, marcado]) => marcado)
      .map(([id]) => Number(id));
  }

  private formularioInicial(): Colaborador {
    return { nome: '', telefone: '', email: '', endereco: '', dataAdmissao: '', statusColaborador: 'ATIVO', funcoesIds: [] };
  }

  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
