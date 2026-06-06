import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { GarantiaApiService } from '../../core/services/garantia-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { GarantiaPeca, GarantiaServico } from '../../models/garantia.model';
import { OrdemServicoResumo } from '../../models/ordem-servico.model';

@Component({ selector: 'app-garantias', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './garantias.component.html' })
export class GarantiasComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  garantiasPecas: GarantiaPeca[] = [];
  garantiasServicos: GarantiaServico[] = [];
  idOrdemSelecionada = 0;
  observacao = '';
  mensagem?: string;
  erro?: string;
  carregando = false;
  processando = false;

  constructor(private readonly garantiaApi: GarantiaApiService, private readonly ordemApi: OrdemServicoApiService) {}
  ngOnInit(): void { this.ordemApi.listar().subscribe({ next: o => this.ordens = o }); this.listarTodas(); }
  listarTodas(): void {
    this.carregando = true;
    this.garantiaApi.listarGarantiasPecas().pipe(finalize(() => this.carregando = false)).subscribe({ next: g => this.garantiasPecas = g, error: e => this.erro = e.message });
    this.garantiaApi.listarGarantiasServicos().subscribe({ next: g => this.garantiasServicos = g, error: e => this.erro = e.message });
  }
  listarPorOrdem(): void {
    if (!this.idOrdemSelecionada) { this.listarTodas(); return; }
    this.carregando = true;
    this.garantiaApi.listarPecasPorOrdemServico(this.idOrdemSelecionada).pipe(finalize(() => this.carregando = false)).subscribe({ next: g => this.garantiasPecas = g, error: e => this.erro = e.message });
    this.garantiaApi.listarServicosPorOrdemServico(this.idOrdemSelecionada).subscribe({ next: g => this.garantiasServicos = g, error: e => this.erro = e.message });
  }
  acionarPeca(g: GarantiaPeca): void { if (!g.id) return; this.processando = true; this.garantiaApi.acionarGarantiaPeca(g.id, { observacao: this.observacao }).pipe(finalize(() => this.processando = false)).subscribe({ next: garantia => { this.mensagem = 'Garantia de peça acionada.'; this.atualizarGarantiaPeca(garantia); this.listarPorOrdem(); }, error: e => this.erro = e.message }); }
  encerrarPeca(g: GarantiaPeca): void { if (!g.id) return; this.processando = true; this.garantiaApi.encerrarGarantiaPeca(g.id, { observacao: this.observacao }).pipe(finalize(() => this.processando = false)).subscribe({ next: garantia => { this.mensagem = 'Garantia de peça encerrada.'; this.atualizarGarantiaPeca(garantia); this.listarPorOrdem(); }, error: e => this.erro = e.message }); }
  acionarServico(g: GarantiaServico): void { if (!g.id) return; this.processando = true; this.garantiaApi.acionarGarantiaServico(g.id, { observacao: this.observacao }).pipe(finalize(() => this.processando = false)).subscribe({ next: garantia => { this.mensagem = 'Garantia de serviço acionada.'; this.atualizarGarantiaServico(garantia); this.listarPorOrdem(); }, error: e => this.erro = e.message }); }
  encerrarServico(g: GarantiaServico): void { if (!g.id) return; this.processando = true; this.garantiaApi.encerrarGarantiaServico(g.id, { observacao: this.observacao }).pipe(finalize(() => this.processando = false)).subscribe({ next: garantia => { this.mensagem = 'Garantia de serviço encerrada.'; this.atualizarGarantiaServico(garantia); this.listarPorOrdem(); }, error: e => this.erro = e.message }); }
  private atualizarGarantiaPeca(garantia: GarantiaPeca | null | undefined): void { if (!garantia?.id) return; this.garantiasPecas = this.garantiasPecas.map(g => g.id === garantia.id ? garantia : g); }
  private atualizarGarantiaServico(garantia: GarantiaServico | null | undefined): void { if (!garantia?.id) return; this.garantiasServicos = this.garantiasServicos.map(g => g.id === garantia.id ? garantia : g); }
}
