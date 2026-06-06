import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { EmpresaTerceirizada } from '../../models/servico.model';

@Injectable({ providedIn: 'root' })
export class EmpresaTerceirizadaApiService extends BaseApiService<EmpresaTerceirizada> {
  constructor(http: HttpClient) {
    super(http, 'empresas-terceirizadas');
  }
}
