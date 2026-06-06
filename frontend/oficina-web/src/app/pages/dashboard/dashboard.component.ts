import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
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
  statusBancoTipo: 'aguardando' | 'ok' | 'erro' = 'aguardando';
  tempoRespostaBancoMs?: number;
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
    this.statusBanco = 'Consultando backend...';
    this.statusBancoTipo = 'aguardando';
    this.tempoRespostaBancoMs = undefined;

    this.dashboardService.verificarBanco()
      .pipe(finalize(() => this.carregandoBanco = false))
      .subscribe({
        next: (status) => {
          this.statusBancoTipo = status.available ? 'ok' : 'erro';
          this.statusBanco = status.mensagem;
          this.tempoRespostaBancoMs = status.tempoRespostaMs;
        },
        error: (error) => {
          this.statusBancoTipo = 'erro';
          this.erro = error.message;
          this.statusBanco = 'Falha na comunicação com a API.';
        }
      });
  }

  private carregarPadroes(): void {
    this.dashboardService.listarPadroesProjeto().subscribe({
      next: (padroes) => this.padroes = padroes,
      error: () => this.padroes = []
    });
  }
}
