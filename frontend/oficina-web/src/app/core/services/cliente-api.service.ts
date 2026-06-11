import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, switchMap, timeout } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { ClienteDetalhe, ClientePessoaFisica, ClientePessoaJuridica, ClienteResumo } from '../../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteApiService extends BaseApiService<ClienteResumo> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'clientes');
  }

  /**
   * Função: Envia ao backend os dados preenchidos na tela para gravação.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  criarPessoaFisica(payload: ClientePessoaFisica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-fisica`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  /**
   * Função: Envia ao backend os dados preenchidos na tela para gravação.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  criarPessoaJuridica(payload: ClientePessoaJuridica): Observable<ClienteResumo> {
    return this.http.post<ApiResponse<ClienteResumo>>(`${this.apiBaseUrl}/clientes/pessoa-juridica`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  buscarDetalhado(id: number): Observable<ClienteDetalhe> {
    return this.http.get<ApiResponse<ClienteDetalhe> | ClienteDetalhe>(
      `${this.apiBaseUrl}/clientes/${id}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(10000),
      /**
       * Função: Executa a integração HTTP necessária para map.
       * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
       * componentes focados na tela.
       */
      map(response => this.extrairDados(response) as ClienteDetalhe)
    );
  }

  /**
   * Função: Envia ao backend a alteração de um registro já existente.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  atualizarPessoaFisica(id: number, payload: ClientePessoaFisica): Observable<ClienteResumo> {
    return this.http.put<ApiResponse<ClienteResumo> | ClienteResumo>(`${this.apiBaseUrl}/clientes/pessoa-fisica/${id}`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  /**
   * Função: Envia ao backend a alteração de um registro já existente.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  atualizarPessoaJuridica(id: number, payload: ClientePessoaJuridica): Observable<ClienteResumo> {
    return this.http.put<ApiResponse<ClienteResumo> | ClienteResumo>(`${this.apiBaseUrl}/clientes/pessoa-juridica/${id}`, payload)
      .pipe(timeout(10000), map(response => this.extrairDados(response) as ClienteResumo));
  }

  /**
   * Função: Monta a chamada HTTP de consulta e normaliza a resposta recebida da API.
   * Uso no sistema: mantém a comunicação com a API centralizada em serviços Angular, deixando os
   * componentes focados na tela.
   */
  inativarEListar(id: number): Observable<ClienteResumo[]> {
    return this.excluir(id).pipe(switchMap(() => this.listar()));
  }
}
