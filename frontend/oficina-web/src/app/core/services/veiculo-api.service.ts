import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { VeiculoResumo } from '../../models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class VeiculoApiService extends BaseApiService<VeiculoResumo> {
  constructor(http: HttpClient) {
    super(http, 'veiculos');
  }
}
