import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, forkJoin, switchMap } from 'rxjs';
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

  constructor(private readonly garantiaApi: GarantiaApiService, private readonly ordemApi: OrdemServicoApiService, private readonly cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.carregarTelaInicial(); }

  carregarTelaInicial(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    forkJoin({ ordens: this.ordemApi.listar(), pecas: this.garantiaApi.listarGarantiasPecas(), servicos: this.garantiaApi.listarGarantiasServicos() })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: r => { this.ordens = [...r.ordens]; this.garantiasPecas = [...r.pecas]; this.garantiasServicos = [...r.servicos]; this.atualizarTela(); },
        error: e => { this.erro = e.message; this.atualizarTela(); }
      });
  }

  listarTodas(): void { this.carregarTelaInicial(); }

  listarPorOrdem(): void {
    if (!this.idOrdemSelecionada) { this.listarTodas(); return; }
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    forkJoin({ pecas: this.garantiaApi.listarPecasPorOrdemServico(this.idOrdemSelecionada), servicos: this.garantiaApi.listarServicosPorOrdemServico(this.idOrdemSelecionada) })
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({ next: r => { this.garantiasPecas = [...r.pecas]; this.garantiasServicos = [...r.servicos]; this.atualizarTela(); }, error: e => { this.erro = e.message; this.atualizarTela(); } });
  }

  acionarPeca(g: GarantiaPeca): void { if (!this.permiteAcionar(g.statusGarantia)) { this.erro = 'Apenas garantias vigentes podem ser acionadas.'; return; } this.executarAcaoPeca(g, 'ACIONAR'); }
  encerrarPeca(g: GarantiaPeca): void { if (!this.permiteEncerrar(g.statusGarantia)) { this.erro = 'Apenas garantias acionadas podem ser encerradas.'; return; } this.executarAcaoPeca(g, 'ENCERRAR'); }
  acionarServico(g: GarantiaServico): void { if (!this.permiteAcionar(g.statusGarantia)) { this.erro = 'Apenas garantias vigentes podem ser acionadas.'; return; } this.executarAcaoServico(g, 'ACIONAR'); }
  encerrarServico(g: GarantiaServico): void { if (!this.permiteEncerrar(g.statusGarantia)) { this.erro = 'Apenas garantias acionadas podem ser encerradas.'; return; } this.executarAcaoServico(g, 'ENCERRAR'); }

  permiteAcionar(status?: string): boolean {
    return status === 'VIGENTE';
  }

  permiteEncerrar(status?: string): boolean {
    return status === 'ACIONADA';
  }

  private executarAcaoPeca(g: GarantiaPeca, tipo: 'ACIONAR' | 'ENCERRAR'): void {
    if (!g.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const acao = tipo === 'ACIONAR' ? this.garantiaApi.acionarGarantiaPeca(g.id, { observacao: this.observacao }) : this.garantiaApi.encerrarGarantiaPeca(g.id, { observacao: this.observacao });
    acao.pipe(switchMap(() => this.idOrdemSelecionada ? this.garantiaApi.listarPecasPorOrdemServico(this.idOrdemSelecionada) : this.garantiaApi.listarGarantiasPecas()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: garantias => { this.mensagem = tipo === 'ACIONAR' ? 'Garantia de peça acionada.' : 'Garantia de peça encerrada.'; this.garantiasPecas = [...garantias]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  private executarAcaoServico(g: GarantiaServico, tipo: 'ACIONAR' | 'ENCERRAR'): void {
    if (!g.id) return;
    this.processando = true; this.erro = undefined; this.atualizarTela();
    const acao = tipo === 'ACIONAR' ? this.garantiaApi.acionarGarantiaServico(g.id, { observacao: this.observacao }) : this.garantiaApi.encerrarGarantiaServico(g.id, { observacao: this.observacao });
    acao.pipe(switchMap(() => this.idOrdemSelecionada ? this.garantiaApi.listarServicosPorOrdemServico(this.idOrdemSelecionada) : this.garantiaApi.listarGarantiasServicos()), finalize(() => { this.processando = false; this.atualizarTela(); })).subscribe({
      next: garantias => { this.mensagem = tipo === 'ACIONAR' ? 'Garantia de serviço acionada.' : 'Garantia de serviço encerrada.'; this.garantiasServicos = [...garantias]; this.atualizarTela(); },
      error: e => { this.erro = e.message; this.atualizarTela(); }
    });
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
