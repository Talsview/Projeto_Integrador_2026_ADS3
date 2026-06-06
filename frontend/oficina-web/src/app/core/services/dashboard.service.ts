import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { PadraoProjeto } from '../../models/padrao-projeto.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly apiBaseUrl = environment.apiBaseUrl;

  constructor(private readonly http: HttpClient) {}

  verificarBanco(): Observable<string> {
    return this.http.get<ApiResponse<string>>(`${this.apiBaseUrl}/database/status`)
      .pipe(map(response => response.message || String(response.data ?? 'Banco consultado.')));
  }

  listarPadroesProjeto(): Observable<PadraoProjeto[]> {
    return this.http.get<ApiResponse<PadraoProjeto[]>>(`${this.apiBaseUrl}/padroes-projeto`)
      .pipe(map(response => response.data ?? []));
  }
}
