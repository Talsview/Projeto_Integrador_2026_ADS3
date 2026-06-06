import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { PadraoProjeto } from '../../models/padrao-projeto.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  statusBanco = 'Aguardando verificação.';
  carregandoBanco = false;
  padroes: PadraoProjeto[] = [];
  erro?: string;

  constructor(private readonly dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.carregarPadroes();
  }

  verificarBanco(): void {
    this.carregandoBanco = true;
    this.erro = undefined;
    this.dashboardService.verificarBanco().subscribe({
      next: (mensagem) => this.statusBanco = mensagem,
      error: (error) => this.erro = error.message,
      complete: () => this.carregandoBanco = false
    });
  }

  private carregarPadroes(): void {
    this.dashboardService.listarPadroesProjeto().subscribe({
      next: (padroes) => this.padroes = padroes,
      error: () => this.padroes = []
    });
  }
}
