import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ItemPeca } from '../../models/peca.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ItemPecaApiService extends BaseApiService<ItemPeca> {
  constructor(http: HttpClient) {
    super(http, 'itens-peca');
  }

  listarPorOrdemServico(idOrdemServico: number): Observable<ItemPeca[]> {
    return this.http.get<ApiResponse<PageResponse<ItemPeca> | ItemPeca[]> | PageResponse<ItemPeca> | ItemPeca[]>(
      `${this.apiBaseUrl}/itens-peca/ordem-servico/${idOrdemServico}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => this.extrairListaDeResposta<ItemPeca>(response))
    );
  }
}
