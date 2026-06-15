import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { EmpresaTerceirizadaApiService } from '../../core/services/empresa-terceirizada-api.service';
import { ServicoApiService } from '../../core/services/servico-api.service';
import { numeroNaoNegativo, textoCadastroValido } from '../../core/validation/field-validation';
import { EmpresaTerceirizada, Servico, TipoServico } from '../../models/servico.model';

@Component({ selector: 'app-servicos', standalone: true, imports: [CommonModule, FormsModule, InativosPanelComponent], templateUrl: './servicos.component.html' })
export class ServicosComponent implements OnInit {
  servicos: Servico[] = [];
  servicosInativos: Servico[] = [];
  empresas: EmpresaTerceirizada[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  termo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  tipos: TipoServico[] = ['INTERNO', 'TERCEIRIZADO'];
  form: Servico = this.formularioInicial();

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly servicoApi: ServicoApiService,
    private readonly empresaApi: EmpresaTerceirizadaApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}
  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void { this.carregarDadosIniciais(); }


  /**
   * Função: carrega serviços e empresas terceirizadas usadas no cadastro.
   * Uso no sistema: permite que serviço terceirizado tenha uma empresa padrão cadastrada, que será
   * preenchida automaticamente quando o serviço for usado na Ordem de Serviço.
   */
  carregarDadosIniciais(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();
    forkJoin({
      servicos: this.servicoApi.listar(),
      empresas: this.empresaApi.listar()
    }).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: r => { this.servicos = [...r.servicos]; this.empresas = [...r.empresas]; this.atualizarTela(); },
      error: e => { this.erro = e.message ?? 'Não foi possível carregar os serviços.'; this.atualizarTela(); }
    });
  }

  listar(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.servicos = [...servicos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisar(): void {
    const t = this.termo.trim();
    if (!t) { this.listar(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.pesquisar(t).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: s => { this.servicos = [...s]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvar(): void {
    this.mensagem = undefined; this.erro = undefined;
    const erroValidacao = this.validarFormulario();
    if (erroValidacao) { this.erro = erroValidacao; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.form.id ? this.servicoApi.atualizar(this.form.id, this.form) : this.servicoApi.criar(this.form);
    acao.pipe(switchMap(() => this.servicoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.mensagem = 'Serviço salvo.'; this.servicos = [...servicos]; this.limpar(); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editar(servico: Servico): void { this.form = { ...servico }; this.atualizarTela(); }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluir(servico: Servico): void {
    if (!servico.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.servicoApi.excluir(servico.id).pipe(switchMap(() => this.servicoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: servicos => { this.mensagem = 'Serviço inativado.'; this.servicos = [...servicos]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  limpar(): void { this.form = this.formularioInicial(); this.atualizarTela(); }

  /**
   * Função: ajusta os campos específicos quando o tipo do serviço muda.
   * Uso no sistema: serviço interno não possui empresa terceirizada padrão; serviço terceirizado
   * precisa da empresa parceira cadastrada para ser preenchida automaticamente na OS.
   */
  aoAlterarTipoServico(): void {
    if (this.form.tipoServico === 'INTERNO') {
      this.form.idEmpresaTerceirizadaPadrao = undefined;
      this.form.observacaoTerceirizacao = '';
    } else {
      this.form.observacaoInterna = '';
    }
    this.atualizarTela();
  }
  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): string | undefined {
    if (!textoCadastroValido(this.form.nomeServico, true)) return 'Informe um nome de serviço válido.';
    if (!this.form.tipoServico) return 'Selecione o tipo do serviço.';
    if (this.form.tipoServico === 'TERCEIRIZADO' && (!this.form.idEmpresaTerceirizadaPadrao || Number(this.form.idEmpresaTerceirizadaPadrao) <= 0)) return 'Selecione a empresa terceirizada padrão do serviço.';
    if (!numeroNaoNegativo(this.form.valorBase)) return 'O valor base do serviço não pode ser negativo.';
    if (!numeroNaoNegativo(this.form.prazoGarantiaDias)) return 'O prazo de garantia não pode ser negativo.';
    return undefined;
  }

  /**
   * Função: Controla na tela a etapa formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private formularioInicial(): Servico { return { nomeServico: '', descricao: '', prazoGarantiaDias: 90, valorBase: 0, tipoServico: 'INTERNO', observacaoInterna: '', observacaoTerceirizacao: '', idEmpresaTerceirizadaPadrao: undefined }; }

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
    this.servicoApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.servicosInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: Servico): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.servicoApi.ativar(registro.id)
      .pipe(switchMap(() => this.servicoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.servicos = [...registros];
          this.mensagem = 'Cadastro ativado.';
          this.carregarInativos();
          this.atualizarTela();
        },
        error: e => { this.erro = e.message ?? 'Não foi possível ativar o cadastro.'; this.atualizarTela(); }
      });
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
