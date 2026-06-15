import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InativosPanelComponent } from '../../shared/components/inativos-panel/inativos-panel.component';
import { Router } from '@angular/router';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { chassiValido, dataFutura, anoVeiculoValido, numeroNaoNegativo, placaValida, textoCadastroValido, normalizarChassi, normalizarPlaca } from '../../core/validation/field-validation';
import { ClienteResumo } from '../../models/cliente.model';
import { Marca, Modelo, VeiculoResumo } from '../../models/veiculo.model';

@Component({ selector: 'app-veiculos', standalone: true, imports: [CommonModule, FormsModule, InativosPanelComponent], templateUrl: './veiculos.component.html' })
export class VeiculosComponent implements OnInit {
  veiculos: VeiculoResumo[] = [];
  veiculosInativos: VeiculoResumo[] = [];
  mostrarInativos = false;
  carregandoInativos = false;
  marcas: Marca[] = [];
  modelos: Modelo[] = [];
  clientes: ClienteResumo[] = [];
  clientesProprietarioFiltrados: ClienteResumo[] = [];
  termo = '';
  termoClienteProprietario = '';
  erro?: string;
  mensagem?: string;
  carregando = false;
  processando = false;
  errosCampo: Record<string, string> = {};

  form: any = this.formularioInicial();

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(
    private readonly veiculoApi: VeiculoApiService,
    private readonly marcaApi: MarcaApiService,
    private readonly modeloApi: ModeloApiService,
    private readonly clienteApi: ClienteApiService,
    private readonly cdr: ChangeDetectorRef,
    private readonly router: Router
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
      veiculos: this.veiculoApi.listar(),
      marcas: this.marcaApi.listar(),
      modelos: this.modeloApi.listar(),
      clientes: this.clienteApi.listar()
    }).pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: resultado => {
        this.veiculos = [...resultado.veiculos];
        this.marcas = [...resultado.marcas];
        this.modelos = [...resultado.modelos];
        this.clientes = [...resultado.clientes];
        this.clientesProprietarioFiltrados = [...resultado.clientes];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível carregar os dados de veículos.';
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: Controla na tela a etapa carregar apoio.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  carregarApoio(): void {
    forkJoin({
      marcas: this.marcaApi.listar(),
      modelos: this.modeloApi.listar(),
      clientes: this.clienteApi.listar()
    }).subscribe({
      next: resultado => {
        this.marcas = [...resultado.marcas];
        this.modelos = [...resultado.modelos];
        this.clientes = [...resultado.clientes];
        this.clientesProprietarioFiltrados = [...resultado.clientes];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível carregar dados de apoio.';
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

    this.veiculoApi.listar().pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: v => {
        this.veiculos = [...v];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível listar veículos.';
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisar(): void {
    const c = this.termo.trim();
    if (!c) { this.listar(); return; }
    this.carregando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.veiculoApi.pesquisar(c).pipe(finalize(() => {
      this.carregando = false;
      this.atualizarTela();
    })).subscribe({
      next: v => {
        this.veiculos = [...v];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível pesquisar veículos.';
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
    if (!this.validarFormulario()) {
      this.erro = 'Corrija os campos destacados antes de salvar o veículo.';
      this.atualizarTela();
      return;
    }
    this.processando = true;
    this.atualizarTela();
    this.veiculoApi.salvarEListar(this.form.id, this.form).pipe(
      finalize(() => {
        this.processando = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: veiculos => {
        this.veiculos = [...veiculos];
        this.mensagem = 'Veículo salvo.';
        this.limpar();
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível salvar o veículo.';
        this.atualizarTela();
      }
    });
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  editar(v: VeiculoResumo): void {
    this.form = { ...v, modeloId: v.modeloId ?? 0, marcaId: v.marcaId ?? 0, proprietarioAtualId: v.proprietarioAtualId ?? 0, quilometragemAtual: v.quilometragemAtual ?? undefined };
    this.termoClienteProprietario = '';
    const atual = this.clientes.find(c => Number(c.id) === Number(v.proprietarioAtualId));
    this.clientesProprietarioFiltrados = atual ? [atual, ...this.clientes.filter(c => c.id !== atual.id)] : [...this.clientes];
    this.atualizarTela();
  }


  /**
   * Função: abre a aba dedicada ao histórico de proprietários já filtrada pelo veículo escolhido.
   * Uso no sistema: facilita a rastreabilidade da posse sem misturar a tela de cadastro de veículos
   * com a tela de consulta histórica.
   */
  abrirHistoricoProprietario(v: VeiculoResumo): void {
    if (!v.id) return;
    this.router.navigate(['/historico-proprietarios'], { queryParams: { veiculoId: v.id } });
  }

  /**
   * Função: Solicita confirmação e envia a inativação do registro para a API.
   * Uso no sistema: remove o item da listagem principal sem apagar seu histórico no banco.
   */
  excluir(v: VeiculoResumo): void {
    if (!v.id) return;
    this.processando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.veiculoApi.excluirEListar(v.id).pipe(
      finalize(() => {
        this.processando = false;
        this.atualizarTela();
      })
    ).subscribe({
      next: veiculos => {
        this.mensagem = 'Veículo inativado.';
        this.veiculos = [...veiculos];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível inativar o veículo.';
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
    this.veiculoApi.listarInativos()
      .pipe(finalize(() => { this.carregandoInativos = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => { this.veiculosInativos = [...registros]; this.atualizarTela(); },
        error: e => { this.erro = e.message ?? 'Não foi possível carregar os inativos.'; this.atualizarTela(); }
      });
  }

  /**
   * Função: Envia à API a reativação do registro escolhido na tela de inativos.
   * Uso no sistema: permite recuperar cadastros sem criar duplicidade.
   */
  ativarInativo(registro: VeiculoResumo): void {
    if (!registro.id) return;
    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.veiculoApi.ativar(registro.id)
      .pipe(switchMap(() => this.veiculoApi.listar()), finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: registros => {
          this.veiculos = [...registros];
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
    this.form = this.formularioInicial();
    this.termoClienteProprietario = '';
    this.clientesProprietarioFiltrados = [...this.clientes];
    this.errosCampo = {};
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa normalizar placa campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  normalizarPlacaCampo(): void {
    this.form.placa = normalizarPlaca(this.form.placa);
    if (this.form.placa && !placaValida(this.form.placa)) {
      this.errosCampo['placa'] = 'Informe uma placa válida no formato ABC1234 ou ABC1D23.';
    } else {
      delete this.errosCampo['placa'];
    }
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa normalizar chassi campo.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  normalizarChassiCampo(): void {
    this.form.chassi = normalizarChassi(this.form.chassi);
    if (this.form.chassi && !chassiValido(this.form.chassi)) {
      this.errosCampo['chassi'] = 'O chassi deve possuir 17 caracteres válidos, sem I, O e Q.';
    } else {
      delete this.errosCampo['chassi'];
    }
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa modelos da marca selecionada.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  modelosDaMarcaSelecionada(): Modelo[] {
    const marcaId = Number(this.form.marcaId ?? 0);
    if (!marcaId) return this.modelos;
    return this.modelos.filter(modelo => Number(modelo.marcaId) === marcaId);
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  pesquisarClientesProprietario(): void {
    const termo = this.termoClienteProprietario.trim().toLowerCase();
    if (!termo) {
      this.clientesProprietarioFiltrados = [...this.clientes];
      this.atualizarTela();
      return;
    }
    this.clientesProprietarioFiltrados = this.clientes.filter(cliente => {
      const nome = (cliente.nome ?? '').toLowerCase();
      const documento = (cliente.documento ?? '').toLowerCase();
      return nome.includes(termo) || documento.includes(termo);
    });
    this.atualizarTela();
  }

  /**
   * Função: Carrega os dados selecionados para o formulário, permitindo conferência ou alteração.
   * Uso no sistema: evita redigitação e mantém a edição vinculada ao registro correto.
   */
  selecionarClienteProprietario(cliente: ClienteResumo): void {
    this.form.proprietarioAtualId = cliente.id ?? 0;
    this.termoClienteProprietario = '';
    this.clientesProprietarioFiltrados = [cliente, ...this.clientes.filter(c => c.id !== cliente.id)];
    delete this.errosCampo['proprietarioAtualId'];
    this.atualizarTela();
  }

  /**
   * Função: Controla na tela a etapa nome proprietario selecionado.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  nomeProprietarioSelecionado(): string {
    const cliente = this.clientes.find(c => Number(c.id) === Number(this.form.proprietarioAtualId));
    return cliente ? `${cliente.nome} — ${cliente.documento ?? ''}` : 'Nenhum proprietário selecionado';
  }

  /**
   * Função: Controla na tela a etapa validar formulario.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private validarFormulario(): boolean {
    this.errosCampo = {};
    if (!this.form.marcaId || Number(this.form.marcaId) <= 0) this.errosCampo['marcaId'] = 'Selecione a marca do veículo.';
    if (!this.form.modeloId || Number(this.form.modeloId) <= 0) this.errosCampo['modeloId'] = 'Selecione o modelo do veículo.';
    if (!placaValida(this.form.placa)) this.errosCampo['placa'] = 'Informe uma placa válida no formato ABC1234 ou ABC1D23.';
    if (!chassiValido(this.form.chassi)) this.errosCampo['chassi'] = 'O chassi deve possuir 17 caracteres válidos, sem I, O e Q.';
    if (this.form.cor && !textoCadastroValido(this.form.cor, false)) this.errosCampo['cor'] = 'A cor do veículo contém caracteres inválidos.';
    if (!anoVeiculoValido(this.form.anoVeiculo, true)) this.errosCampo['anoVeiculo'] = 'Informe um ano de fabricação válido.';
    if (!anoVeiculoValido(this.form.anoModelo, true)) this.errosCampo['anoModelo'] = 'Informe um ano modelo válido.';
    if (Number(this.form.anoModelo) < Number(this.form.anoVeiculo) - 1 || Number(this.form.anoModelo) > Number(this.form.anoVeiculo) + 1) this.errosCampo['anoModelo'] = 'O ano modelo deve ser coerente com o ano de fabricação.';
    if (!numeroNaoNegativo(this.form.quilometragemAtual)) this.errosCampo['quilometragemAtual'] = 'A quilometragem não pode ser negativa.';
    if (!this.form.proprietarioAtualId || Number(this.form.proprietarioAtualId) <= 0) this.errosCampo['proprietarioAtualId'] = 'Selecione o proprietário atual do veículo.';
    if (dataFutura(this.form.dataInicioPosse)) this.errosCampo['dataInicioPosse'] = 'A data de início da posse não pode ser futura.';
    return Object.keys(this.errosCampo).length === 0;
  }

  /**
   * Função: Controla na tela a etapa formulario inicial.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private formularioInicial(): any {
    return { modeloId: 0, marcaId: 0, placa: '', chassi: '', cor: '', anoVeiculo: undefined, anoModelo: undefined, quilometragemAtual: undefined, observacao: '', proprietarioAtualId: 0, dataInicioPosse: '', observacaoPosse: '' };
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
