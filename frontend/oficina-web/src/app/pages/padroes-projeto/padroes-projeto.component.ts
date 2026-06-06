import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../core/services/dashboard.service';
import { PadraoProjeto } from '../../models/padrao-projeto.model';

@Component({
  selector: 'app-padroes-projeto',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './padroes-projeto.component.html'
})
export class PadroesProjetoComponent implements OnInit {
  padroes: PadraoProjeto[] = [];
  erro?: string;

  constructor(private readonly dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.listarPadroesProjeto().subscribe({
      next: padroes => this.padroes = padroes,
      error: error => this.erro = error.message
    });
  }
}
