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

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(private readonly ordemApi: OrdemServicoApiService, private readonly cdr: ChangeDetectorRef) {}

  /**
   * Função: Inicializa a tela carregando listas, filtros e dados necessários para o primeiro uso.
   * Uso no sistema: prepara o estado visual antes da interação do usuário.
   */
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

  /**
   * Função: Controla na tela a etapa ordenar.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
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

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
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

  /**
   * Função: Recalcula valores exibidos na tela conforme quantidade, peça, serviço ou valor unitário
   * informado.
   * Uso no sistema: mantém o orçamento visual coerente antes de enviar os itens para a API.
   */
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

  /**
   * Função: Recalcula valores exibidos na tela conforme quantidade, peça, serviço ou valor unitário
   * informado.
   * Uso no sistema: mantém o orçamento visual coerente antes de enviar os itens para a API.
   */
  calcularTotalDaLinha(ordem: OrdemServicoResumo): void {
    if (!ordem.id) return;
    this.idTotal = ordem.id;
    this.calcularTotalRecursivo();
  }

  /**
   * Função: Aciona a mudança de etapa da Ordem de Serviço conforme o fluxo operacional permitido.
   * Uso no sistema: impede salto indevido entre Orçamento, Execução, Pagamento e Finalizado.
   */
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

  /**
   * Função: Controla na tela a etapa classe prioridade.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  classePrioridade(prioridade?: string): string {
    const valor = (prioridade ?? '').toUpperCase();
    if (valor === 'URGENTE' || valor === 'ALTA') return 'danger';
    if (valor === 'NORMAL') return 'info';
    return 'success';
  }

  /**
   * Função: Atualiza os filtros da tela e recarrega a lista com os registros compatíveis.
   * Uso no sistema: facilita localizar clientes, veículos, OS, peças ou cadastros inativos.
   */
  private filtrarOrdensPorStatus(ordens: OrdemServicoResumo[], status: StatusFluxoOrdemServico): OrdemServicoResumo[] {
    return [...(ordens ?? [])].filter(os => this.normalizarStatus(os.statusAtual) === status);
  }

  /**
   * Função: Controla na tela a etapa normalizar status.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private normalizarStatus(status?: string): StatusFluxoOrdemServico | '' {
    return (status ?? '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toUpperCase()
      .replace(/\s+/g, '_') as StatusFluxoOrdemServico | '';
  }

  /**
   * Função: Controla na tela a etapa atualizar tela.
   * Uso no sistema: mantém a regra visual separada da regra de negócio executada pelo backend.
   */
  private atualizarTela(): void { this.cdr.detectChanges(); }
}
