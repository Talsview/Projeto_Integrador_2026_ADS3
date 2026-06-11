import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ItemServico } from '../../models/ordem-servico.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ItemServicoApiService extends BaseApiService<ItemServico> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'itens-servico');
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarPorOrdemServico(idOrdemServico: number): Observable<ItemServico[]> {
    return this.http.get<ApiResponse<PageResponse<ItemServico> | ItemServico[]> | PageResponse<ItemServico> | ItemServico[]>(
      `${this.apiBaseUrl}/itens-servico/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairListaDeResposta<ItemServico>(response))
    );
  }
}
