import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ItemPeca } from '../../models/peca.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ItemPecaApiService extends BaseApiService<ItemPeca> {
  constructor(http: HttpClient) {
    super(http, 'itens-peca');
  }

  listarPorOrdemServico(idOrdemServico: number): Observable<ItemPeca[]> {
    return this.http.get<ApiResponse<PageResponse<ItemPeca> | ItemPeca[]>>(`${this.apiBaseUrl}/itens-peca/ordem-servico/${idOrdemServico}`)
      .pipe(map(response => {
        const dados = this.extrairDados(response);
        if (!dados) return [];
        if (Array.isArray(dados)) return dados;
        return dados.content ?? [];
      }));
  }
}
