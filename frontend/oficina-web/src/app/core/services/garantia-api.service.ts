import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ApiResponse, PageResponse } from '../models/api-response.model';
import { AcionamentoGarantia, GarantiaPeca, GarantiaServico } from '../../models/garantia.model';

@Injectable({ providedIn: 'root' })
export class GarantiaApiService extends BaseApiService<GarantiaPeca | GarantiaServico> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'garantias');
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarGarantiasPecas(): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaPeca> | GarantiaPeca[]> | PageResponse<GarantiaPeca> | GarantiaPeca[]>(
      `${this.apiBaseUrl}/garantias/pecas`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaPeca>(response)));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarGarantiasServicos(): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaServico> | GarantiaServico[]> | PageResponse<GarantiaServico> | GarantiaServico[]>(
      `${this.apiBaseUrl}/garantias/servicos`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaServico>(response)));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarPecasPorOrdemServico(idOrdemServico: number): Observable<GarantiaPeca[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaPeca> | GarantiaPeca[]> | PageResponse<GarantiaPeca> | GarantiaPeca[]>(
      `${this.apiBaseUrl}/garantias/pecas/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaPeca>(response)));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarServicosPorOrdemServico(idOrdemServico: number): Observable<GarantiaServico[]> {
    return this.http.get<ApiResponse<PageResponse<GarantiaServico> | GarantiaServico[]> | PageResponse<GarantiaServico> | GarantiaServico[]>(
      `${this.apiBaseUrl}/garantias/servicos/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairListaDeResposta<GarantiaServico>(response)));
  }

  /**
   * Função: Executa a integração HTTP necessária para acionar garantia peca.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  acionarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca> | GarantiaPeca>(`${this.apiBaseUrl}/garantias/pecas/${id}/acionar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaPeca));
  }

  /**
   * Função: Executa a integração HTTP necessária para encerrar garantia peca.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  encerrarGarantiaPeca(id: number, payload: AcionamentoGarantia): Observable<GarantiaPeca> {
    return this.http.patch<ApiResponse<GarantiaPeca> | GarantiaPeca>(`${this.apiBaseUrl}/garantias/pecas/${id}/encerrar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaPeca));
  }

  /**
   * Função: Executa a integração HTTP necessária para acionar garantia servico.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  acionarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico> | GarantiaServico>(`${this.apiBaseUrl}/garantias/servicos/${id}/acionar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaServico));
  }

  /**
   * Função: Executa a integração HTTP necessária para encerrar garantia servico.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  encerrarGarantiaServico(id: number, payload: AcionamentoGarantia): Observable<GarantiaServico> {
    return this.http.patch<ApiResponse<GarantiaServico> | GarantiaServico>(`${this.apiBaseUrl}/garantias/servicos/${id}/encerrar`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as GarantiaServico));
  }
}
