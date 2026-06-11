import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { textoCadastroValido } from '../../core/validation/field-validation';
import { Marca, Modelo } from '../../models/veiculo.model';

@Component({ selector: 'app-marcas-modelos', standalone: true, imports: [CommonModule, FormsModule, InativosPanelComponent], templateUrl: './marcas-modelos.component.html' })
export class MarcasModelosComponent implements OnInit {
  marcas: Marca[] = [];
  modelos: Modelo[] = [];
  marcasInativas: Marca[] = [];
  modelosInativos: Modelo[] = [];
  mostrarMarcasInativas = false;
  mostrarModelosInativos = false;
  carregandoInativos = false;
  termoMarca = '';
  termoModelo = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;
  marcaForm: Marca = { nomeMarca: '' };
  modeloForm: Modelo = { marcaId: 0, nomeModelo: '' };

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly marcaApi: MarcaApiService,
    private readonly modeloApi: ModeloApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
  ngOnInit(): void { this.listarTudo(); }

  listarTudo(): void {
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();
    /**
     * Função: Controla na tela a etapa fork join.
     * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
     */
    forkJoin({ marcas: this.marcaApi.listar(), modelos: this.modeloApi.listar() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: resultado => { this.marcas = [...resultado.marcas]; this.modelos = [...resultado.modelos]; this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  /**
   * Função: Controla na tela a etapa listar marcas.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  listarMarcas(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Controla na tela a etapa listar modelos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  listarModelos(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.listar().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisarMarcas(): void {
    const termo = this.termoMarca.trim();
    if (!termo) { this.listarMarcas(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.pesquisar(termo).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisarModelos(): void {
    const termo = this.termoModelo.trim();
    if (!termo) { this.listarModelos(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.pesquisar(termo).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvarMarca(): void {
    this.mensagem = undefined; this.erro = undefined;
    if (!textoCadastroValido(this.marcaForm.nomeMarca, true)) { this.erro = 'Informe um nome de marca válido.'; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.marcaForm.id ? this.marcaApi.atualizar(this.marcaForm.id, this.marcaForm) : this.marcaApi.criar(this.marcaForm);
    acao.pipe(switchMap(() => this.marcaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.mensagem = 'Marca salva.'; this.marcas = [...marcas]; this.marcaForm = { nomeMarca: '' }; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Valida os campos da tela, envia os dados para a API e atualiza a listagem após a
   * gravação.
   * Uso no sistema: concentra o fluxo de cadastro/edição iniciado pelo usuário.
   */
  salvarModelo(): void {
    this.mensagem = undefined; this.erro = undefined;
    if (!this.modeloForm.marcaId || Number(this.modeloForm.marcaId) <= 0) { this.erro = 'Selecione a marca do modelo.'; this.atualizarTela(); return; }
    if (!textoCadastroValido(this.modeloForm.nomeModelo, true)) { this.erro = 'Informe um nome de modelo válido.'; this.atualizarTela(); return; }
    this.processando = true; this.atualizarTela();
    const acao = this.modeloForm.id ? this.modeloApi.atualizar(this.modeloForm.id, this.modeloForm) : this.modeloApi.criar(this.modeloForm);
    acao.pipe(switchMap(() => this.modeloApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.mensagem = 'Modelo salvo.'; this.modelos = [...modelos]; this.modeloForm = { marcaId: 0, nomeModelo: '' }; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  editarMarca(marca: Marca): void { this.marcaForm = { ...marca }; this.atualizarTela(); }
  editarModelo(modelo: Modelo): void { this.modeloForm = { ...modelo }; this.atualizarTela(); }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluirMarca(marca: Marca): void {
    if (!marca.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.excluir(marca.id).pipe(switchMap(() => this.marcaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.mensagem = 'Marca inativada.'; this.marcas = [...marcas]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluirModelo(modelo: Modelo): void {
    if (!modelo.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.excluir(modelo.id).pipe(switchMap(() => this.modeloApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.mensagem = 'Modelo inativado.'; this.modelos = [...modelos]; this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }



  /**
   * Função: Controla na tela a etapa abrir marcas inativas.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirMarcasInativas(): void { this.mostrarMarcasInativas = true; this.carregarMarcasInativas(); }

  /**
   * Função: Controla na tela a etapa abrir modelos inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  abrirModelosInativos(): void { this.mostrarModelosInativos = true; this.carregarModelosInativos(); }

  /**
   * Função: Fecha painel, modal ou menu aberto e retorna a tela ao estado padrão.
   * Uso no sistema: controla a navegação visual sem alterar dados do banco.
   */
  fecharInativos(): void { this.mostrarMarcasInativas = false; this.mostrarModelosInativos = false; this.atualizarTela(); }

  /**
   * Função: Controla na tela a etapa carregar marcas inativas.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarMarcasInativas(): void {
    this.carregandoInativos = true; this.erro = undefined; this.atualizarTela();
    this.marcaApi.listarInativos().pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcasInativas = [...marcas]; this.atualizarTela(); },
      error: e => { this.erro = e.message ?? 'Não foi possível carregar marcas inativas.'; this.atualizarTela(); }
    });
  }

  /**
   * Função: Controla na tela a etapa carregar modelos inativos.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarModelosInativos(): void {
    this.carregandoInativos = true; this.erro = undefined; this.atualizarTela();
    this.modeloApi.listarInativos().pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelosInativos = [...modelos]; this.atualizarTela(); },
      error: e => { this.erro = e.message ?? 'Não foi possível carregar modelos inativos.'; this.atualizarTela(); }
    });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarMarcaInativa(marca: Marca): void {
    if (!marca.id) return;
    this.processando = true; this.erro = undefined; this.mensagem = undefined; this.atualizarTela();
    this.marcaApi.ativar(marca.id).pipe(switchMap(() => this.marcaApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: marcas => { this.marcas = [...marcas]; this.mensagem = 'Cadastro ativado.'; this.carregarMarcasInativas(); this.atualizarTela(); },
      error: e => { this.erro = e.message ?? 'Não foi possível ativar a marca.'; this.atualizarTela(); }
    });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarModeloInativo(modelo: Modelo): void {
    if (!modelo.id) return;
    this.processando = true; this.erro = undefined; this.mensagem = undefined; this.atualizarTela();
    this.modeloApi.ativar(modelo.id).pipe(switchMap(() => this.modeloApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: modelos => { this.modelos = [...modelos]; this.mensagem = 'Cadastro ativado.'; this.carregarModelosInativos(); this.atualizarTela(); },
      error: e => { this.erro = e.message ?? 'Não foi possível ativar o modelo.'; this.atualizarTela(); }
    });
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
