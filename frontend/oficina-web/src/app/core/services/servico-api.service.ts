import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { Servico } from '../../models/servico.model';

@Injectable({ providedIn: 'root' })
export class ServicoApiService extends BaseApiService<Servico> {
  constructor(http: HttpClient) {
    super(http, 'servicos');
  }
}
