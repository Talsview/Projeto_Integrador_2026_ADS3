import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, switchMap, timeout } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { ClienteDetalhe, ClientePessoaFisica, ClientePessoaJuridica, ClienteResumo } from '../../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteApiService extends BaseApiService<ClienteResumo> {
  constructor(http: HttpClient) {
    super(http, 'clientes');
  }

  criarPessoaFisica(payload: ClientePessoaFisica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-fisica`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  criarPessoaJuridica(payload: ClientePessoaJuridica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-juridica`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  buscarDetalhado(id: number): Observable<ClienteDetalhe> {
    return this.http.get<ApiResponse<ClienteDetalhe> | ClienteDetalhe>(
      `${this.apiBaseUrl}/clientes/${id}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(10000),
      map(response => this.extrairDados(response) as ClienteDetalhe)
    );
  }

  atualizarPessoaFisica(id: number, payload: ClientePessoaFisica): Observable<ClienteResumo> {
    return this.http.put<ApiResponse<ClienteResumo> | ClienteResumo>(`${this.apiBaseUrl}/clientes/pessoa-fisica/${id}`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  atualizarPessoaJuridica(id: number, payload: ClientePessoaJuridica): Observable<ClienteResumo> {
    return this.http.put<ApiResponse<ClienteResumo> | ClienteResumo>(`${this.apiBaseUrl}/clientes/pessoa-juridica/${id}`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  inativarEListar(id: number): Observable<ClienteResumo[]> {
    return this.excluir(id).pipe(switchMap(() => this.listar()));
  }
}
