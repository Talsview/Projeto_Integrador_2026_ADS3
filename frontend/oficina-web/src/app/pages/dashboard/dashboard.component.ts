import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { DashboardService } from '../../core/services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  statusBanco = 'Aguardando verificação.';
  statusBancoTipo: 'aguardando' | 'ok' | 'erro' = 'aguardando';
  tempoRespostaBancoMs?: number;
  carregandoBanco = false;
  erro?: string;

  constructor(private readonly dashboardService: DashboardService) {}

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
}
