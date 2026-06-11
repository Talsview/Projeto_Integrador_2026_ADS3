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

  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(private readonly http: HttpClient) {}

  /**
   * Função: Executa a integração HTTP necessária para verificar banco.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
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
          mensagem: response.message ?? response.mensagem ?? this.criarMensagemBanco(available, tempoRespostaMs),
          dataHoraVerificacao: String(dados['dataHoraVerificacao'] ?? ''),
          dataHoraUltimaMudancaStatus: String(dados['dataHoraUltimaMudancaStatus'] ?? ''),
          verificacoesRealizadas: Number(dados['verificacoesRealizadas'] ?? 0),
          falhasConsecutivas: Number(dados['falhasConsecutivas'] ?? 0),
          ultimoErro: String(dados['ultimoErro'] ?? ''),
          cacheUtilizado: Boolean(dados['cacheUtilizado'])
        };
      }),
      catchError((error: Error) => of({
        available: false,
        mensagem: this.tratarErroDeComunicacao(error),
        tempoRespostaMs: 0
      }))
    );
  }

  /**
   * Função: Envia ao backend os dados preenchidos na tela para gravação.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  private criarMensagemBanco(available: boolean, tempoRespostaMs: number): string {
    if (available) {
      return `Conectado em ${tempoRespostaMs} ms.`;
    }

    return 'Banco indisponível.';
  }

  /**
   * Função: Executa a integração HTTP necessária para tratar erro de comunicacao.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  private tratarErroDeComunicacao(error: Error): string {
    const mensagem = error.message?.toLowerCase() ?? '';

    if (mensagem.includes('timeout')) {
      return 'Backend sem resposta.';
    }

    if (mensagem.includes('unknown error') || mensagem.includes('http failure response')) {
      return 'Backend indisponível.';
    }

    return error.message || 'Falha ao consultar a API.';
  }
}
