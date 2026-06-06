import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { MarcaApiService } from '../../core/services/marca-api.service';
import { ModeloApiService } from '../../core/services/modelo-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { ClienteResumo } from '../../models/cliente.model';
import { Marca, Modelo, VeiculoResumo } from '../../models/veiculo.model';

@Component({ selector: 'app-veiculos', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './veiculos.component.html' })
export class VeiculosComponent implements OnInit {
  veiculos: VeiculoResumo[] = [];
  marcas: Marca[] = [];
  modelos: Modelo[] = [];
  clientes: ClienteResumo[] = [];
  termo = '';
  erro?: string;
  mensagem?: string;
  carregando = false;
  processando = false;

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
    this.processando = true;
    this.atualizarTela();

    const acao = this.form.id ? this.veiculoApi.atualizar(this.form.id, this.form) : this.veiculoApi.criar(this.form);
    acao.pipe(
      switchMap(() => this.veiculoApi.listar()),
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
    this.form = { ...v, modeloId: v.modeloId ?? 0, marcaId: v.marcaId ?? 0, proprietarioAtualId: v.proprietarioAtualId ?? 0 };
    this.atualizarTela();
  }

  excluir(v: VeiculoResumo): void {
    if (!v.id) return;
    this.processando = true;
    this.erro = undefined;
    this.atualizarTela();

    this.veiculoApi.excluir(v.id).pipe(
      switchMap(() => this.veiculoApi.listar()),
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
    this.atualizarTela();
  }

  private formularioInicial(): any {
    return { modeloId: 0, marcaId: 0, placa: '', chassi: '', cor: '', anoVeiculo: undefined, anoModelo: undefined, quilometragemAtual: 0, observacao: '', proprietarioAtualId: 0, dataInicioPosse: '', observacaoPosse: '' };
  }

  private atualizarTela(): void {
    this.cdr.detectChanges();
  }
}
