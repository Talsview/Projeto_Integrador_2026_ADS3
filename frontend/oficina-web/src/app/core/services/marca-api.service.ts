import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { Marca } from '../../models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class MarcaApiService extends BaseApiService<Marca> {
  constructor(http: HttpClient) {
    super(http, 'marcas');
  }
}
