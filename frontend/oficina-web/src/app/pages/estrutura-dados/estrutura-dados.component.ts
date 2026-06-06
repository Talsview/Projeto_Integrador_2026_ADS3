import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrdemServicoApiService } from '../../core/services/ordem-servico-api.service';
import { OrdemServicoResumo } from '../../models/ordem-servico.model';

@Component({
  selector: 'app-estrutura-dados',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estrutura-dados.component.html'
})
export class EstruturaDadosComponent {
  ordens: OrdemServicoResumo[] = [];
  termo = '';
  idTotal?: number;
  totalRecursivo?: string;
  erro?: string;

  constructor(private readonly ordemApi: OrdemServicoApiService) {}

  carregarFila(): void {
    this.ordemApi.filaAtendimento().subscribe({
      next: ordens => this.ordens = ordens,
      error: error => this.erro = error.message
    });
  }

  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): void {
    this.ordemApi.ordenar(criterio).subscribe({
      next: ordens => this.ordens = ordens,
      error: error => this.erro = error.message
    });
  }

  pesquisarLinear(): void {
    this.ordemApi.pesquisarLinear(this.termo).subscribe({
      next: ordens => this.ordens = ordens,
      error: error => this.erro = error.message
    });
  }

  calcularTotalRecursivo(): void {
    if (!this.idTotal) {
      this.erro = 'Informe o ID da Ordem de Serviço.';
      return;
    }
    this.ordemApi.totalRecursivo(this.idTotal).subscribe({
      next: total => this.totalRecursivo = JSON.stringify(total),
      error: error => this.erro = error.message
    });
  }
}
