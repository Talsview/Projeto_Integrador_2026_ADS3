import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { ItemServico } from '../../models/ordem-servico.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ItemServicoApiService extends BaseApiService<ItemServico> {
  constructor(http: HttpClient) {
    super(http, 'itens-servico');
  }

  listarPorOrdemServico(idOrdemServico: number): Observable<ItemServico[]> {
    return this.http.get<ApiResponse<PageResponse<ItemServico> | ItemServico[]>>(`${this.apiBaseUrl}/itens-servico/ordem-servico/${idOrdemServico}`)
      .pipe(map(response => {
        const dados = this.extrairDados(response);
        if (!dados) return [];
        if (Array.isArray(dados)) return dados;
        return dados.content ?? [];
      }));
  }
}
