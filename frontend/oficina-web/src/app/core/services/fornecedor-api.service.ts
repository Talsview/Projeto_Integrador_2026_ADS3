import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { Fornecedor } from '../../models/peca.model';

@Injectable({ providedIn: 'root' })
export class FornecedorApiService extends BaseApiService<Fornecedor> {
  constructor(http: HttpClient) {
    super(http, 'fornecedores');
  }
}
