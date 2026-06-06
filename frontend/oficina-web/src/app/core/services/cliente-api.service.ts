import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { ClientePessoaFisica, ClientePessoaJuridica, ClienteResumo } from '../../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteApiService extends BaseApiService<ClienteResumo> {
  constructor(http: HttpClient) {
    super(http, 'clientes');
  }

  criarPessoaFisica(payload: ClientePessoaFisica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-fisica`, payload)
      .pipe(map(response => this.extrairDados(response) as ClienteResumo));
  }

  criarPessoaJuridica(payload: ClientePessoaJuridica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-juridica`, payload)
      .pipe(map(response => this.extrairDados(response) as ClienteResumo));
  }
}
