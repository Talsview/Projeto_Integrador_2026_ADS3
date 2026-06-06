import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
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

  form: any = { modeloId: 0, marcaId: 0, placa: '', chassi: '', cor: '', anoVeiculo: undefined, anoModelo: undefined, quilometragemAtual: 0, observacao: '', proprietarioAtualId: 0, dataInicioPosse: '', observacaoPosse: '' };

  constructor(
    private readonly veiculoApi: VeiculoApiService,
    private readonly marcaApi: MarcaApiService,
    private readonly modeloApi: ModeloApiService,
    private readonly clienteApi: ClienteApiService
  ) {}

  ngOnInit(): void { this.listar(); this.carregarApoio(); }
  carregarApoio(): void { this.marcaApi.listar().subscribe({ next: m => this.marcas = m }); this.modeloApi.listar().subscribe({ next: m => this.modelos = m }); this.clienteApi.listar().subscribe({ next: c => this.clientes = c }); }
  listar(): void { this.carregando = true; this.erro = undefined; this.veiculoApi.listar().pipe(finalize(() => this.carregando = false)).subscribe({ next: v => this.veiculos = v, error: e => this.erro = e.message }); }
  pesquisar(): void { const c = this.termo.trim(); if (!c) { this.listar(); return; } this.carregando = true; this.erro = undefined; this.veiculoApi.pesquisar(c).pipe(finalize(() => this.carregando = false)).subscribe({ next: v => this.veiculos = v, error: e => this.erro = e.message }); }
  salvar(): void { this.mensagem = undefined; this.erro = undefined; this.processando = true; const a = this.form.id ? this.veiculoApi.atualizar(this.form.id, this.form) : this.veiculoApi.criar(this.form); a.pipe(finalize(() => this.processando = false)).subscribe({ next: veiculo => { this.mensagem = 'Veículo salvo com sucesso.'; this.inserirOuAtualizar(veiculo as VeiculoResumo); this.limpar(); this.listar(); }, error: e => this.erro = e.message }); }
  editar(v: VeiculoResumo): void { this.form = { ...v, modeloId: v.modeloId ?? 0, marcaId: v.marcaId ?? 0, proprietarioAtualId: v.proprietarioAtualId ?? 0 }; }
  excluir(v: VeiculoResumo): void { if (!v.id) return; this.processando = true; this.veiculoApi.excluir(v.id).pipe(finalize(() => this.processando = false)).subscribe({ next: () => { this.mensagem = 'Veículo inativado.'; this.veiculos = this.veiculos.filter(item => item.id !== v.id); this.listar(); }, error: e => this.erro = e.message }); }
  limpar(): void { this.form = { modeloId: 0, marcaId: 0, placa: '', chassi: '', cor: '', anoVeiculo: undefined, anoModelo: undefined, quilometragemAtual: 0, observacao: '', proprietarioAtualId: 0, dataInicioPosse: '', observacaoPosse: '' }; }

  private inserirOuAtualizar(veiculo: VeiculoResumo | null | undefined): void {
    if (!veiculo?.id) return;
    const existe = this.veiculos.some(item => item.id === veiculo.id);
    this.veiculos = existe ? this.veiculos.map(item => item.id === veiculo.id ? veiculo : item) : [veiculo, ...this.veiculos];
  }
}
