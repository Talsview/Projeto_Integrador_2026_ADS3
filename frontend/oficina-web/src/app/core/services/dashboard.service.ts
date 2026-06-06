import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { DatabaseStatus } from '../models/database-status.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly apiBaseUrl = environment.apiBaseUrl;
  private readonly tempoLimiteMs = 3000;

  constructor(private readonly http: HttpClient) {}

  verificarBanco(): Observable<DatabaseStatus> {
    return this.http.get<ApiResponse<Record<string, unknown>>>(`${this.apiBaseUrl}/database/status`).pipe(
      timeout(this.tempoLimiteMs),
      map((response) => {
        const dados = response.data ?? response.dados ?? {};
        const available = Boolean(dados['available']);
        const tempoRespostaMs = Number(dados['tempoRespostaMs'] ?? 0);

        return {
          available,
          tempoRespostaMs,
          mensagem: response.message ?? response.mensagem ?? this.criarMensagemBanco(available, tempoRespostaMs)
        };
      }),
      catchError((error: Error) => of({
        available: false,
        mensagem: this.tratarErroDeComunicacao(error),
        tempoRespostaMs: 0
      }))
    );
  }

  private criarMensagemBanco(available: boolean, tempoRespostaMs: number): string {
    if (available) {
      return `Backend e banco responderam em ${tempoRespostaMs} ms.`;
    }

    return 'Backend respondeu, mas a conexão com o banco de dados não está disponível.';
  }

  private tratarErroDeComunicacao(error: Error): string {
    const mensagem = error.message?.toLowerCase() ?? '';

    if (mensagem.includes('timeout')) {
      return 'Tempo limite atingido. Verifique se o backend está rodando na porta 9081.';
    }

    if (mensagem.includes('unknown error') || mensagem.includes('http failure response')) {
      return 'Não foi possível comunicar com o backend. Confirme se o Spring Boot está iniciado.';
    }

    return error.message || 'Falha ao consultar a API.';
  }
}
