import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { Funcao } from '../../models/pessoa.model';

@Injectable({ providedIn: 'root' })
export class FuncaoApiService extends BaseApiService<Funcao> {
  constructor(http: HttpClient) {
    super(http, 'funcoes');
  }
}
