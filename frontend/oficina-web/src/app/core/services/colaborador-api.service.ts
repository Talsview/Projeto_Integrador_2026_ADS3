import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { ColaboradorResumo } from '../../models/pessoa.model';

@Injectable({ providedIn: 'root' })
export class ColaboradorApiService extends BaseApiService<ColaboradorResumo> {
  /**
   * Função: Recebe os serviços necessários para esta classe, como HttpClient, APIs ou dependências
   * de navegação.
   * Uso no sistema: permite que o Angular injete dependências sem criação manual dentro dos métodos.
   */
  constructor(http: HttpClient) {
    super(http, 'colaboradores');
  }
}
