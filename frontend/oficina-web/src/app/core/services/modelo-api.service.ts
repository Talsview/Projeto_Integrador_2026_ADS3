import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { BaseApiService } from './base-api.service';
import { Modelo } from '../../models/veiculo.model';
import { ApiResponse, PageResponse } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class ModeloApiService extends BaseApiService<Modelo> {
  constructor(http: HttpClient) {
    super(http, 'modelos');
  }

  listarPorMarca(marcaId: number): Observable<Modelo[]> {
    return this.http.get<ApiResponse<PageResponse<Modelo> | Modelo[]>>(`${this.apiBaseUrl}/modelos/marca/${marcaId}`)
      .pipe(map(response => {
        const dados = this.extrairDados(response);
        if (!dados) return [];
        if (Array.isArray(dados)) return dados;
        return dados.content ?? [];
      }));
  }
}
