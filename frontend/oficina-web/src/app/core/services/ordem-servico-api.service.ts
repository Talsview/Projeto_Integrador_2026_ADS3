import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { AlterarStatusOrdemServico, OrdemServicoResumo, TotalRecursivoOrdemServico } from '../../models/ordem-servico.model';

@Injectable({ providedIn: 'root' })
export class OrdemServicoApiService extends BaseApiService<OrdemServicoResumo> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'ordens-servico');
  }

  /**
   * Função: Executa a integração HTTP necessária para alterar status.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  alterarStatus(id: number, payload: AlterarStatusOrdemServico): Observable<OrdemServicoResumo> {
    return this.http.patch<ApiResponse<OrdemServicoResumo> | OrdemServicoResumo>(`${this.apiBaseUrl}/ordens-servico/${id}/status`, payload)
      .pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as OrdemServicoResumo));
  }

  /**
   * Função: Executa a integração HTTP necessária para enviar orcamento para execucao.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  enviarOrcamentoParaExecucao(id: number): Observable<OrdemServicoResumo> {
    return this.http.patch<ApiResponse<OrdemServicoResumo> | OrdemServicoResumo>(
      `${this.apiBaseUrl}/ordens-servico/${id}/enviar-para-execucao`,
      null
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as OrdemServicoResumo));
  }

  /**
   * Função: Executa a integração HTTP necessária para enviar orcamento para pagamento.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  enviarOrcamentoParaPagamento(id: number): Observable<OrdemServicoResumo> {
    return this.enviarOrcamentoParaExecucao(id);
  }

  /**
   * Função: Executa a integração HTTP necessária para baixar nota fiscal pdf.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  baixarNotaFiscalPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiBaseUrl}/notas-fiscais/ordens-servico/${id}/pdf`, {
      responseType: 'blob',
      headers: this.noCacheHeaders,
      params: this.parametrosSemCache()
    }).pipe(timeout(this.tempoLimiteMs));
  }

  /**
   * Função: Executa a integração HTTP necessária para fila atendimento.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  filaAtendimento(): Observable<OrdemServicoResumo[]> {
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/fila-atendimento`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  /**
   * Função: Executa a integração HTTP necessária para ordenar.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  ordenar(criterio: 'DATA_ABERTURA' | 'VALOR_TOTAL' | 'PRIORIDADE'): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('criterio', criterio);
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/ordenar`,
      this.opcoesSemCache(params)
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  pesquisarLinear(termo: string): Observable<OrdemServicoResumo[]> {
    const params = new HttpParams().set('termo', termo ?? '');
    return this.http.get<ApiResponse<any> | any>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/pesquisar-linear`,
      this.opcoesSemCache(params)
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairOrdensEstruturaDados(this.extrairDados(response))));
  }

  /**
   * Função: Executa a integração HTTP necessária para total recursivo.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  totalRecursivo(id: number): Observable<TotalRecursivoOrdemServico> {
    return this.http.get<ApiResponse<TotalRecursivoOrdemServico> | TotalRecursivoOrdemServico>(
      `${this.apiBaseUrl}/estrutura-dados/ordens-servico/${id}/total-recursivo`,
      this.opcoesSemCache()
    ).pipe(timeout(this.tempoLimiteMs), map(response => this.extrairDados(response) as TotalRecursivoOrdemServico));
  }

  /**
   * Função: Executa a integração HTTP necessária para extrair ordens estrutura dados.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
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
