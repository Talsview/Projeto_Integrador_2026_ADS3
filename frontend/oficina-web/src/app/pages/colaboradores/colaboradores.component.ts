import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ColaboradorApiService } from '../../core/services/colaborador-api.service';
import { dataFutura, emailValido, formatarTelefone, nomePessoaValido, telefoneValido } from '../../core/validation/field-validation';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { Colaborador, ColaboradorFuncao, ColaboradorResumo, Funcao, StatusColaborador } from '../../models/pessoa.model';

@Component({ selector: 'app-colaboradores', standalone: true, imports: [CommonModule, FormsModule, InativosPanelComponent], templateUrl: './colaboradores.component.html' })
export class ColaboradoresComponent implements OnInit {
  colaboradores: ColaboradorResumo[] = [];
  colaboradoresInativos: ColaboradorResumo[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
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

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly colaboradorApi: ColaboradorApiService,
    private readonly funcaoApi: FuncaoApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void {
    this.carregarTelaInicial();
  }

  /**
   * Função: Controla na tela a etapa carregar tela inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
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

  /**
   * Função: Controla na tela a etapa listar.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
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

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
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

  /**
   * Função: Controla na tela a etapa is funcao selecionada.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  isFuncaoSelecionada(id?: number): boolean {
    return id ? Boolean(this.funcoesSelecionadas[id]) : false;
  }

  /**
   * Função: Controla na tela a etapa set funcao selecionada.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  setFuncaoSelecionada(id: number | undefined, marcado: boolean): void {
    if (!id) return;
    this.funcoesSelecionadas[id] = marcado;
    this.validarFuncoesSelecionadas();
    this.atualizarTela();
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
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

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
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

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
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


  /**
   * Função: Controla na tela a etapa abrir inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirInativos(): void {
    this.mostrarInativos = true;
    this.carregarInativos();
  }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  fecharInativos(): void {
    this.mostrarInativos = false;
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa carregar inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarInativos(): void {
    this.carregandoInativos = true;
    this.erro = undefined;
    this.atualizarTela();
    this.colaboradorApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.colaboradoresInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: ColaboradorResumo): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.colaboradorApi.ativar(registro.id)
      .pipe(switchMap(() => this.colaboradorApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.colaboradores = [...registros];
          this.mensagem = 'Cadastro ativado.';
          this.carregarInativos();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível ativar o cadastro.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Limpa formulário, filtros ou estados temporários usados na tela.
   * Uso no sistema: permite iniciar um novo cadastro ou consulta sem dados anteriores interferindo.
   */
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

  /**
   * Função: Controla na tela a etapa formatar telefone campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  formatarTelefoneCampo(): void {
    this.form.telefone = formatarTelefone(this.form.telefone);
    this.validarTelefoneSePreenchido();
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa validar nome.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarNome(): void {
    if (!nomePessoaValido(this.form.nome)) {
      this.errosCampo['nome'] = 'Informe um nome válido, sem números ou caracteres especiais indevidos.';
      return;
    }
    delete this.errosCampo['nome'];
  }

  /**
   * Função: Controla na tela a etapa validar telefone se preenchido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarTelefoneSePreenchido(): void {
    if (!telefoneValido(this.form.telefone)) {
      this.errosCampo['telefone'] = 'Informe somente números no telefone, com DDD. Exemplo: (62) 99999-9999.';
      return;
    }
    delete this.errosCampo['telefone'];
  }

  /**
   * Função: Controla na tela a etapa validar email se preenchido.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarEmailSePreenchido(): void {
    if (!emailValido(this.form.email)) {
      this.errosCampo['email'] = 'Informe um e-mail válido.';
      return;
    }
    delete this.errosCampo['email'];
  }

  /**
   * Função: Controla na tela a etapa validar data admissao.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarDataAdmissao(): void {
    if (dataFutura(this.form.dataAdmissao)) {
      this.errosCampo['dataAdmissao'] = 'A data de admissão não pode ser futura.';
      return;
    }
    delete this.errosCampo['dataAdmissao'];
  }

  /**
   * Função: Controla na tela a etapa validar funcoes selecionadas.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  validarFuncoesSelecionadas(): void {
    if (this.obterFuncoesSelecionadas().length === 0) {
      this.errosCampo['funcoes'] = 'Selecione pelo menos uma função para o colaborador.';
      return;
    }
    delete this.errosCampo['funcoes'];
  }

  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): boolean {
    this.errosCampo = {};
    this.validarNome();
    this.validarTelefoneSePreenchido();
    this.validarEmailSePreenchido();
    this.validarDataAdmissao();
    this.validarFuncoesSelecionadas();
    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: Controla na tela a etapa obter funcoes selecionadas.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private obterFuncoesSelecionadas(): number[] {
    return Object.entries(this.funcoesSelecionadas)
      .filter(([, marcado]) => marcado)
      .map(([id]) => Number(id));
  }

  /**
   * Função: Controla na tela a etapa formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private formularioInicial(): Colaborador {
    return { nome: '', telefone: '', email: '', endereco: '', dataAdmissao: '', statusColaborador: 'ATIVO', funcoesIds: [] };
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
