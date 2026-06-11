import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { chassiValido, dataFutura, anoVeiculoValido, numeroNaoNegativo, placaValida, textoCadastroValido, normalizarChassi, normalizarPlaca } from '../../core/validation/field-validation';
import { ClienteResumo } from '../../models/cliente.model';
import { Marca, Modelo, VeiculoResumo } from '../../models/veiculo.model';

@Component({ selector: 'app-veiculos', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './veiculos.component.html' })
export class VeiculosComponent implements OnInit {
  veiculos: VeiculoResumo[] = [];
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

  constructor(
    private readonly veiculoApi: VeiculoApiService,
    private readonly marcaApi: MarcaApiService,
    private readonly modeloApi: ModeloApiService,
    private readonly clienteApi: ClienteApiService,
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
        this.mensagem = 'Veículo salvo com sucesso. A tabela foi atualizada automaticamente.';
        this.limpar();
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível salvar o veículo.';
        this.atualizarTela();
      }
    });
  }

  editar(v: VeiculoResumo): void {
    this.form = { ...v, modeloId: v.modeloId ?? 0, marcaId: v.marcaId ?? 0, proprietarioAtualId: v.proprietarioAtualId ?? 0, quilometragemAtual: v.quilometragemAtual ?? undefined };
    this.termoClienteProprietario = '';
    const atual = this.clientes.find(c => Number(c.id) === Number(v.proprietarioAtualId));
    this.clientesProprietarioFiltrados = atual ? [atual, ...this.clientes.filter(c => c.id !== atual.id)] : [...this.clientes];
    this.atualizarTela();
  }

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
        this.mensagem = 'Veículo inativado. A tabela foi atualizada automaticamente.';
        this.veiculos = [...veiculos];
        this.atualizarTela();
      },
      error: e => {
        this.erro = e.message ?? 'Não foi possível inativar o veículo.';
        this.atualizarTela();
      }
    });
  }

  limpar(): void {
    this.form = this.formularioInicial();
    this.termoClienteProprietario = '';
    this.clientesProprietarioFiltrados = [...this.clientes];
    this.errosCampo = {};
    this.atualizarTela();
  }

  normalizarPlacaCampo(): void {
    this.form.placa = normalizarPlaca(this.form.placa);
    if (this.form.placa && !placaValida(this.form.placa)) {
      this.errosCampo['placa'] = 'Informe uma placa válida no formato ABC1234 ou ABC1D23.';
    } else {
      delete this.errosCampo['placa'];
    }
    this.atualizarTela();
  }

  normalizarChassiCampo(): void {
    this.form.chassi = normalizarChassi(this.form.chassi);
    if (this.form.chassi && !chassiValido(this.form.chassi)) {
      this.errosCampo['chassi'] = 'O chassi deve possuir 17 caracteres válidos, sem I, O e Q.';
    } else {
      delete this.errosCampo['chassi'];
    }
    this.atualizarTela();
  }

  modelosDaMarcaSelecionada(): Modelo[] {
    const marcaId = Number(this.form.marcaId ?? 0);
    if (!marcaId) return this.modelos;
    return this.modelos.filter(modelo => Number(modelo.marcaId) === marcaId);
  }

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

  selecionarClienteProprietario(cliente: ClienteResumo): void {
    this.form.proprietarioAtualId = cliente.id ?? 0;
    this.termoClienteProprietario = '';
    this.clientesProprietarioFiltrados = [cliente, ...this.clientes.filter(c => c.id !== cliente.id)];
    delete this.errosCampo['proprietarioAtualId'];
    this.atualizarTela();
  }

  nomeProprietarioSelecionado(): string {
    const cliente = this.clientes.find(c => Number(c.id) === Number(this.form.proprietarioAtualId));
    return cliente ? `${cliente.nome} — ${cliente.documento ?? ''}` : 'Nenhum proprietário selecionado';
  }

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

  private formularioInicial(): any {
    return { modeloId: 0, marcaId: 0, placa: '', chassi: '', cor: '', anoVeiculo: undefined, anoModelo: undefined, quilometragemAtual: undefined, observacao: '', proprietarioAtualId: 0, dataInicioPosse: '', observacaoPosse: '' };
  }

  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
