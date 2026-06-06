import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { ClienteApiService } from '../../core/services/cliente-api.service';
import { DashboardService } from '../../core/services/dashboard.service';
import { GarantiaApiService } from '../../core/services/garantia-api.service';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { VeiculoApiService } from '../../core/services/veiculo-api.service';
import { OrdemServicoResumo } from '../../models/ordem-servico.model';

interface IndicadorOficina {
  titulo: string;
  valor: number | string;
  detalhe: string;
  classe: 'primary' | 'success' | 'warning' | 'info' | 'danger';
  icone: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  indicadores: IndicadorOficina[] = [];
  ultimasOrdens: OrdemServicoResumo[] = [];
  carregandoVisaoGeral = false;

  totalOrdens = 0;
  totalOrcamento = 0;
  totalExecucao = 0;
  totalPagamento = 0;
  totalFinalizado = 0;
  faturamentoEstimado = 0;
  dataAtual = new Date();

  statusBanco = 'Backend local';
  statusBancoTipo: 'aguardando' | 'ok' | 'erro' = 'aguardando';
  tempoRespostaBancoMs?: number;
  carregandoBanco = false;
  erro?: string;

  constructor(
    private readonly dashboardService: DashboardService,
    private readonly clienteService: ClienteApiService,
    private readonly veiculoService: VeiculoApiService,
    private readonly ordemServicoService: OrdemServicoApiService,
    private readonly garantiaService: GarantiaApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarVisaoGeral();
    this.verificarBanco();
  }

  carregarVisaoGeral(): void {
    this.carregandoVisaoGeral = true;

    forkJoin({
      clientes: this.clienteService.listar().pipe(catchError(() => of([]))),
      veiculos: this.veiculoService.listar().pipe(catchError(() => of([]))),
      ordens: this.ordemServicoService.listar().pipe(catchError(() => of([]))),
      garantiasPecas: this.garantiaService.listarGarantiasPecas().pipe(catchError(() => of([]))),
      garantiasServicos: this.garantiaService.listarGarantiasServicos().pipe(catchError(() => of([])))
    })
      .pipe(finalize(() => {
        this.carregandoVisaoGeral = false;
        this.cdr.detectChanges();
      }))
      .subscribe(({ clientes, veiculos, ordens, garantiasPecas, garantiasServicos }) => {
        this.totalOrdens = ordens.length;
        this.totalOrcamento = ordens.filter(os => this.normalizarStatus(os.statusAtual) === 'ORCAMENTO').length;
        this.totalExecucao = ordens.filter(os => this.normalizarStatus(os.statusAtual) === 'EXECUCAO').length;
        this.totalPagamento = ordens.filter(os => this.normalizarStatus(os.statusAtual) === 'PAGAMENTO').length;
        this.totalFinalizado = ordens.filter(os => this.normalizarStatus(os.statusAtual) === 'FINALIZADO').length;

        const abertas = ordens.filter(os => this.normalizarStatus(os.statusAtual) !== 'FINALIZADO').length;
        const garantiasAtivas = [...garantiasPecas, ...garantiasServicos]
          .filter(g => ['VIGENTE', 'ACIONADA', 'ATIVA'].includes(String(g.statusGarantia).toUpperCase())).length;
        this.faturamentoEstimado = ordens
          .filter(os => this.normalizarStatus(os.statusAtual) === 'FINALIZADO')
          .reduce((total, os) => total + Number(os.valorTotal || 0), 0);

        this.indicadores = [
          { titulo: 'OS Abertas', valor: abertas, detalhe: 'Atendimentos não finalizados', classe: 'primary', icone: 'OS' },
          { titulo: 'OS em Execução', valor: this.totalExecucao, detalhe: 'Serviços em andamento', classe: 'success', icone: 'EX' },
          { titulo: 'Aguardando Pagamento', valor: this.totalPagamento, detalhe: 'OS prontas para quitação', classe: 'warning', icone: 'PG' },
          { titulo: 'Garantias Ativas', valor: garantiasAtivas, detalhe: 'Peças e serviços cobertos', classe: 'info', icone: 'GT' },
          { titulo: 'Clientes', valor: clientes.length, detalhe: 'Pessoas físicas e jurídicas', classe: 'primary', icone: 'CL' },
          { titulo: 'Veículos', valor: veiculos.length, detalhe: 'Com histórico de propriedade', classe: 'success', icone: 'VE' }
        ];

        this.ultimasOrdens = [...ordens]
          .sort((a, b) => String(b.dataAbertura ?? '').localeCompare(String(a.dataAbertura ?? '')))
          .slice(0, 6);
      });
  }

  verificarBanco(): void {
    this.carregandoBanco = true;
    this.erro = undefined;
    this.statusBanco = 'Verificando backend...';
    this.statusBancoTipo = 'aguardando';
    this.tempoRespostaBancoMs = undefined;

    this.dashboardService.verificarBanco()
      .pipe(finalize(() => {
        this.carregandoBanco = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: (status) => {
          this.statusBancoTipo = status.available ? 'ok' : 'erro';
          this.statusBanco = status.available ? 'Backend conectado' : status.mensagem;
          this.tempoRespostaBancoMs = status.tempoRespostaMs;
        },
        error: (error) => {
          this.statusBancoTipo = 'erro';
          this.erro = error.message;
          this.statusBanco = 'Falha na API';
        }
      });
  }

  percentual(valor: number): number {
    if (!this.totalOrdens) {
      return 0;
    }
    return Math.round((valor / this.totalOrdens) * 100);
  }

  classeBadgeStatus(status?: string): string {
    switch (this.normalizarStatus(status)) {
      case 'FINALIZADO': return 'success';
      case 'PAGAMENTO': return 'info';
      case 'EXECUCAO': return 'warning';
      default: return '';
    }
  }

  private normalizarStatus(status?: string): string {
    return String(status || 'ORCAMENTO').toUpperCase();
  }
}
