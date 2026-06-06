import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { AlterarStatusOrdemServico, OrdemServicoResumo } from '../../models/ordem-servico.model';

@Injectable({ providedIn: 'root' })
export class OrdemServicoApiService extends BaseApiService<OrdemServicoResumo> {
  constructor(http: HttpClient) {
    super(http, 'ordens-servico');
  }

  alterarStatus(id: number, payload: AlterarStatusOrdemServico): Observable<OrdemServicoResumo> {
    return this.http.patch<ApiResponse<OrdemServicoResumo>>(`${this.apiBaseUrl}/ordens-servico/${id}/status`, payload)
      .pipe(map(response => response.data));
  }

  filaAtendimento(): Observable<OrdemServicoResumo[]> {
    return this.http.get<ApiResponse<any>>(`${this.apiBaseUrl}/estrutura-dados/ordens-servico/fila-atendimento`)
      .pipe(map(response => this.extrairOrdensEstruturaDados(response.data)));
  }

  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('criterio', criterio);
    return this.http.get<ApiResponse<any>>(`${this.apiBaseUrl}/estrutura-dados/ordens-servico/ordenar`, { params })
      .pipe(map(response => this.extrairOrdensEstruturaDados(response.data)));
  }

  pesquisarLinear(termo: string): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('termo', termo ?? '');
    return this.http.get<ApiResponse<any>>(`${this.apiBaseUrl}/estrutura-dados/ordens-servico/pesquisar-linear`, { params })
      .pipe(map(response => this.extrairOrdensEstruturaDados(response.data)));
  }

  totalRecursivo(id: number): Observable<unknown> {
    return this.http.get<ApiResponse<unknown>>(`${this.apiBaseUrl}/estrutura-dados/ordens-servico/${id}/total-recursivo`)
      .pipe(map(response => response.data));
  }

  private extrairOrdensEstruturaDados(data: any): OrdemServicoResumo[] {
    if (!data) {
      return [];
    }
    if (Array.isArray(data)) {
      return data;
    }
    if (Array.isArray(data.ordens)) {
      return data.ordens;
    }
    if (Array.isArray(data.itens)) {
      return data.itens;
    }
    if (Array.isArray(data.resultados)) {
      return data.resultados;
    }
    if (Array.isArray(data.content)) {
      return data.content;
    }
    return [];
  }
}
