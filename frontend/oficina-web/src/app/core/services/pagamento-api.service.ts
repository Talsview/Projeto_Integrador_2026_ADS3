import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { Pagamento, ResumoPagamentoOrdemServico, StatusPagamento } from '../../models/pagamento.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class PagamentoApiService extends BaseApiService<Pagamento> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'pagamentos');
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarPorOrdemServico(idOrdemServico: number): Observable<Pagamento[]> {
    return this.http.get<ApiResponse<PageResponse<Pagamento> | Pagamento[]> | PageResponse<Pagamento> | Pagamento[]>(
      `${this.apiBaseUrl}/pagamentos/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<Pagamento>(response)));
  }

  /**
   * Função: Executa a integração HTTP necessária para resumo por ordem servico.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  resumoPorOrdemServico(idOrdemServico: number): Observable<ResumoPagamentoOrdemServico> {
    return this.http.get<ApiResponse<ResumoPagamentoOrdemServico> | ResumoPagamentoOrdemServico>(
      `${this.apiBaseUrl}/pagamentos/ordem-servico/${idOrdemServico}/resumo`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as ResumoPagamentoOrdemServico));
  }

  /**
   * Função: Executa a integração HTTP necessária para alterar status.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  alterarStatus(id: number, statusPagamento: StatusPagamento): Observable<Pagamento> {
    const params = new HttpParams().set('statusPagamento', statusPagamento);
    return this.http.patch<ApiResponse<Pagamento> | Pagamento>(
      `${this.apiBaseUrl}/pagamentos/${id}/status`,
      null,
      this.opcoesSemCache(params)
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as Pagamento));
  }
}
