import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { ColaboradorResumo } from '../../models/pessoa.model';

@Injectable({ providedIn: 'root' })
export class ColaboradorApiService extends BaseApiService<ColaboradorResumo> {
  constructor(http: HttpClient) {
    super(http, 'colaboradores');
  }
}
