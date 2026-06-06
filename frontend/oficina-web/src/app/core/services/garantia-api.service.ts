import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PageResponse } from '../models/api-response.model';
import { AcionamentoGarantia, GarantiaPeca, GarantiaServico } from '../../models/garantia.model';

@Injectable({ providedIn: 'root' })
export class GarantiaApiService {
  private readonly apiBaseUrl = environment.apiBaseUrl;

  constructor(private readonly http: HttpClient) {}

  listarGarantiasPecas(): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaPeca> | GarantiaPeca[]>>(`${this.apiBaseUrl}/garantias/pecas`)
      .pipe(map(response => this.extrairLista(response.data ?? response.dados)));
  }

  listarGarantiasServicos(): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaServico> | GarantiaServico[]>>(`${this.apiBaseUrl}/garantias/servicos`)
      .pipe(map(response => this.extrairLista(response.data ?? response.dados)));
  }

  listarPecasPorOrdemServico(idOrdemServico: number): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<GarantiaPeca[]>>(`${this.apiBaseUrl}/garantias/pecas/ordem-servico/${idOrdemServico}`)
      .pipe(map(response => response.data ?? response.dados ?? []));
  }

  listarServicosPorOrdemServico(idOrdemServico: number): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<GarantiaServico[]>>(`${this.apiBaseUrl}/garantias/servicos/ordem-servico/${idOrdemServico}`)
      .pipe(map(response => response.data ?? response.dados ?? []));
  }

  acionarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca>>(`${this.apiBaseUrl}/garantias/pecas/${id}/acionar`, payload)
      .pipe(map(response => (response.data ?? response.dados) as GarantiaPeca));
  }

  encerrarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca>>(`${this.apiBaseUrl}/garantias/pecas/${id}/encerrar`, payload)
      .pipe(map(response => (response.data ?? response.dados) as GarantiaPeca));
  }

  acionarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico>>(`${this.apiBaseUrl}/garantias/servicos/${id}/acionar`, payload)
      .pipe(map(response => (response.data ?? response.dados) as GarantiaServico));
  }

  encerrarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico>>(`${this.apiBaseUrl}/garantias/servicos/${id}/encerrar`, payload)
      .pipe(map(response => (response.data ?? response.dados) as GarantiaServico));
  }

  private extrairLista<T>(dados: PageResponse<T> | T[] | null | undefined): T[] {
    if (!dados) return [];
    if (Array.isArray(dados)) return dados;
    return dados.content ?? [];
  }
}
