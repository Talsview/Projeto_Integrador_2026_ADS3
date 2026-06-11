import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { EmpresaTerceirizada } from '../../models/servico.model';

@Injectable({ providedIn: 'root' })
export class EmpresaTerceirizadaApiService extends BaseApiService<EmpresaTerceirizada> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'empresas-terceirizadas');
  }
}
