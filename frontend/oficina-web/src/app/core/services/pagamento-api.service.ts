import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { Pagamento, ResumoPagamentoOrdemServico, StatusPagamento } from '../../models/pagamento.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class PagamentoApiService extends BaseApiService<Pagamento> {
  private readonly tempoLimiteMs = 10000;

  constructor(http: HttpClient) {
    super(http, 'pagamentos');
  }

  listarPorOrdemServico(idOrdemServico: number): Observable<Pagamento[]> {
    return this.http.get<ApiResponse<PageResponse<Pagamento> | Pagamento[]>>(`${this.apiBaseUrl}/pagamentos/ordem-servico/${idOrdemServico}`)
      .pipe(timeout(this.tempoLimiteMs), map(response => {
        const dados = this.extrairDados(response);
        if (!dados) return [];
        if (Array.isArray(dados)) return dados;
        return dados.content ?? [];
      }));
  }

  resumoPorOrdemServico(idOrdemServico: number): Observable<ResumoPagamentoOrdemServico> {
    return this.http.get<ApiResponse<ResumoPagamentoOrdemServico>>(`${this.apiBaseUrl}/pagamentos/ordem-servico/${idOrdemServico}/resumo`)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as ResumoPagamentoOrdemServico));
  }

  alterarStatus(id: number, statusPagamento: StatusPagamento): Observable<Pagamento> {
    const params = new HttpParams().set('statusPagamento', statusPagamento);
    return this.http.patch<ApiResponse<Pagamento>>(`${this.apiBaseUrl}/pagamentos/${id}/status`, null, { params })
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as Pagamento));
  }
}
