import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, switchMap } from 'rxjs';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { OrdemServicoResumo, StatusFluxoOrdemServico, TotalRecursivoOrdemServico } from '../../models/ordem-servico.model';

@Component({ selector: 'app-estrutura-dados', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './estrutura-dados.component.html' })
export class EstruturaDadosComponent implements OnInit {
  ordens: OrdemServicoResumo[] = [];
  termo = '';
  idTotal?: number;
  totalRecursivo?: TotalRecursivoOrdemServico;
  erro?: string;
  mensagem?: string;
  carregando = false;
  processando = false;
  readonly statusFila: StatusFluxoOrdemServico = 'EXECUCAO';

  constructor(private readonly ordemApi: OrdemServicoApiService, private readonly cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.carregarFila(); }

  carregarFila(): void {
    this.carregando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.ordemApi.filaAtendimento()
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): void {
    this.carregando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.ordemApi.ordenar(criterio)
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  pesquisarLinear(): void {
    this.carregando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();
    this.ordemApi.pesquisarLinear(this.termo)
      .pipe(finalize(() => { this.carregando = false; this.atualizarTela(); }))
      .subscribe({
        next: ordens => { this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila); this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  calcularTotalRecursivo(): void {
    if (!this.idTotal || Number(this.idTotal) <= 0) {
      this.erro = 'Informe o ID da Ordem de Serviço para calcular o total recursivo.';
      this.atualizarTela();
      return;
    }

    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.totalRecursivo = undefined;
    this.atualizarTela();

    this.ordemApi.totalRecursivo(Number(this.idTotal))
      .pipe(finalize(() => { this.processando = false; this.atualizarTela(); }))
      .subscribe({
        next: total => { this.totalRecursivo = total; this.mensagem = 'Cálculo atualizado.'; this.atualizarTela(); },
        error: error => { this.erro = error.message; this.atualizarTela(); }
      });
  }

  calcularTotalDaLinha(ordem: OrdemServicoResumo): void {
    if (!ordem.id) return;
    this.idTotal = ordem.id;
    this.calcularTotalRecursivo();
  }

  enviarParaPagamento(ordem: OrdemServicoResumo): void {
    if (!ordem.id) return;

    this.processando = true;
    this.erro = undefined;
    this.mensagem = undefined;
    this.atualizarTela();

    this.ordemApi.alterarStatus(ordem.id, {
      novoStatus: 'PAGAMENTO',
      observacao: 'Execução concluída.'
    }).pipe(
      switchMap(() => this.ordemApi.filaAtendimento()),
      finalize(() => { this.processando = false; this.atualizarTela(); })
    ).subscribe({
      next: ordens => {
        this.ordens = this.filtrarOrdensPorStatus(ordens, this.statusFila);
        this.mensagem = 'Enviado para pagamento.';
        this.atualizarTela();
      },
      error: error => { this.erro = error.message; this.atualizarTela(); }
    });
  }

  classePrioridade(prioridade?: string): string {
    const valor = (prioridade ?? '').toUpperCase();
    if (valor === 'URGENTE' || valor === 'ALTA') return 'danger';
    if (valor === 'NORMAL') return 'info';
    return 'success';
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
