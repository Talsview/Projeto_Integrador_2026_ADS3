import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { OrdemServicoResumo, StatusFluxoOrdemServico } from '../../models/ordem-servico.model';

@Component({ selector: 'app-estrutura-dados', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './estrutura-dados.component.html' })
export class EstruturaDadosComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  termo = '';
  idTotal?: number;
  totalRecursivo?: string;
  erro?: string;
  carregando = false;
  readonly statusFila: StatusFluxoOrdemServico = 'EXECUCAO';

  constructor(private readonly ordemApi: OrdemServicoApiService, private readonly cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.carregarFila(); }

  carregarFila(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.ordemApi.filaAtendimento().pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.ordemApi.ordenar(criterio).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  pesquisarLinear(): void {
    this.carregando = true; this.erro = undefined; this.atualizarTela();
    this.ordemApi.pesquisarLinear(this.termo).pipe(finalize(() => { this.carregando = false; this.atualizarTela(); })).subscribe({
      next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  calcularTotalRecursivo(): void {
    if (!this.idTotal) { this.erro = 'Informe o ID da Ordem de Serviço.'; this.atualizarTela(); return; }
    this.ordemApi.totalRecursivo(this.idTotal).subscribe({
      next: total => { this.totalRecursivo = JSON.stringify(total); this.atualizarTela(); },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  private filtrarOrdensPorStatus(ordens: OrdemServicoResumo[], status: StatusFluxoOrdemServico): OrdemServicoResumo[] {
    return [...(ordens ?? [])].filter(os => this.normalizarStatus(os.statusAtual) === status);
  }

  private normalizarStatus(status?: string): StatusFluxoOrdemServico | '' {
    return (status ?? '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toUpperCase()
      .replace(/\s+/g, '_') as StatusFluxoOrdemServico | '';
  }

  private atualizarTela(): void { this.cdr.detectChanges(); }
}
