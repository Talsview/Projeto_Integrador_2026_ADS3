import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { Modelo } from '../../models/veiculo.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ModeloApiService extends BaseApiService<Modelo> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'modelos');
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  listarPorMarca(marcaId: number): Observable<Modelo[]> {
    return this.http.get<ApiResponse<PageResponse<Modelo> | Modelo[]> | PageResponse<Modelo> | Modelo[]>(
      `${this.apiBaseUrl}/modelos/marca/${marcaId}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairListaDeResposta<Modelo>(response))
    );
  }
}
