import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { AlterarStatusOrdemServico, OrdemServicoResumo } from '../../models/ordem-servico.model';

@Injectable({ providedIn: 'root' })
export class OrdemServicoApiService extends BaseApiService<OrdemServicoResumo> {
  constructor(http: HttpClient) {
    super(http, 'ordens-servico');
  }

  alterarStatus(id: number, payload: AlterarStatusOrdemServico): Observable<OrdemServicoResumo> {
    return this.http.patch<ApiResponse<OrdemServicoResumo> | OrdemServicoResumo>(`${this.apiBaseUrl}/ordens-servico/${id}/status`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as OrdemServicoResumo));
  }

  filaAtendimento(): Observable<OrdemServicoResumo[]> {
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/fila-atendimento`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('criterio', criterio);
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/ordenar`,
      this.opcoesSemCache(params)
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  pesquisarLinear(termo: string): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('termo', termo ?? '');
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/pesquisar-linear`,
      this.opcoesSemCache(params)
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  totalRecursivo(id: number): Observable<unknown> {
    return this.http.get<ApiResponse<unknown> | unknown>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/${id}/total-recursivo`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response)));
  }

  private extrairOrdensEstruturaDados(data: any): OrdemServicoResumo[] {
    if (!data) return [];
    if (Array.isArray(data)) return [...data];
    if (Array.isArray(data.ordens)) return [...data.ordens];
    if (Array.isArray(data.itens)) return [...data.itens];
    if (Array.isArray(data.resultados)) return [...data.resultados];
    if (Array.isArray(data.content)) return [...data.content];
    return [];
  }
}
