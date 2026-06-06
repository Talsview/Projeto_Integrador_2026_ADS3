import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ApiResponse, PageResponse } from '../models/api-response.model';
import { AcionamentoGarantia, GarantiaPeca, GarantiaServico } from '../../models/garantia.model';

@Injectable({ providedIn: 'root' })
export class GarantiaApiService extends BaseApiService<GarantiaPeca | GarantiaServico> {
  constructor(http: HttpClient) {
    super(http, 'garantias');
  }

  listarGarantiasPecas(): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaPeca> | GarantiaPeca[]> | PageResponse<GarantiaPeca> | GarantiaPeca[]>(
      `${this.apiBaseUrl}/garantias/pecas`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaPeca>(response)));
  }

  listarGarantiasServicos(): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaServico> | GarantiaServico[]> | PageResponse<GarantiaServico> | GarantiaServico[]>(
      `${this.apiBaseUrl}/garantias/servicos`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaServico>(response)));
  }

  listarPecasPorOrdemServico(idOrdemServico: number): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaPeca> | GarantiaPeca[]> | PageResponse<GarantiaPeca> | GarantiaPeca[]>(
      `${this.apiBaseUrl}/garantias/pecas/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaPeca>(response)));
  }

  listarServicosPorOrdemServico(idOrdemServico: number): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaServico> | GarantiaServico[]> | PageResponse<GarantiaServico> | GarantiaServico[]>(
      `${this.apiBaseUrl}/garantias/servicos/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaServico>(response)));
  }

  acionarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca> | GarantiaPeca>(`${this.apiBaseUrl}/garantias/pecas/${id}/acionar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaPeca));
  }

  encerrarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca> | GarantiaPeca>(`${this.apiBaseUrl}/garantias/pecas/${id}/encerrar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaPeca));
  }

  acionarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico> | GarantiaServico>(`${this.apiBaseUrl}/garantias/servicos/${id}/acionar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaServico));
  }

  encerrarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico> | GarantiaServico>(`${this.apiBaseUrl}/garantias/servicos/${id}/encerrar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaServico));
  }
}
