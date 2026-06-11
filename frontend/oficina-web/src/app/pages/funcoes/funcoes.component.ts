import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, switchMap } from 'rxjs';
import { FuncaoApiService } from '../../core/services/funcao-api.service';
import { textoCadastroValido } from '../../core/validation/field-validation';
import { Funcao } from '../../models/pessoa.model';

@Component({
  selector: 'app-funcoes',
  standalone: true,
  imports: [CommonModule, FormsModule, InativosPanelComponent],
  templateUrl: './funcoes.component.html'
})
export class FuncoesComponent implements OnInit {
  funcoes: Funcao[] = [];
  funcoesInativos: Funcao[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  termo = '';
  carregando = false;
  processando = false;
  sincronizandoTabela = false;
  mensagem?: string;
  erro?: string;

  form: Funcao = { nomeFuncao: '', descricao: '' };

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly funcaoApi: FuncaoApiService,
    private readonly changeDetector: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
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

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvar(): void {
    this.mensagem = undefined;
    this.erro = undefined;
    const erroValidacao = this.validarFormulario();
    if (erroValidacao) {
      this.erro = erroValidacao;
      this.atualizarTela();
      return;
    }
    this.processando = true;
    this.sincronizandoTabela = true;
    this.atualizarTela();
    this.funcaoApi.salvarEListar(this.form.id, this.form).pipe(
      finalize(() => {
        this.processando = false;
        this.sincronizandoTabela = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: funcoesAtualizadas => {
        this.funcoes = [...funcoesAtualizadas];
        this.mensagem = 'Função salva.';
        this.limpar();
        this.atualizarTela();
      },
      error: error => {
        this.erro = error.message;
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  editar(funcao: Funcao): void {
    this.form = { ...funcao };
    this.atualizarTela();
  }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluir(funcao: Funcao): void {
    if (!funcao.id) return;
    this.mensagem = undefined;
    this.erro = undefined;
    this.processando = true;
    this.sincronizandoTabela = true;
    this.atualizarTela();

    this.funcaoApi.excluirEListar(funcao.id).pipe(
        finalize(() => {
          this.processando = false;
          this.sincronizandoTabela = false;
          this.atualizarTela();
        })
      )
      .subscribe({
        next: funcoesAtualizadas => {
          this.funcoes = [...funcoesAtualizadas];
          this.mensagem = 'Função inativada.';
          this.atualizarTela();
        },
        error: error => {
          this.erro = error.message;
          this.atualizarTela();
        }
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
    this.funcaoApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.funcoesInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: Funcao): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.funcaoApi.ativar(registro.id)
      .pipe(switchMap(() => this.funcaoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.funcoes = [...registros];
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
  limpar(): void {
    this.form = { nomeFuncao: '', descricao: '' };
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): string | undefined {
    if (!textoCadastroValido(this.form.nomeFuncao, true)) return 'Informe um nome de função válido.';
    if ((this.form.descricao ?? '').length > 255) return 'A descrição da função deve possuir no máximo 255 caracteres.';
    return undefined;
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void {
    this.changeDetector.detectChanges();
  }
}
