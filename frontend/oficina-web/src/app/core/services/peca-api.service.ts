import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api.service';
import { Peca } from '../../models/peca.model';

@Injectable({ providedIn: 'root' })
export class PecaApiService extends BaseApiService<Peca> {
  constructor(http: HttpClient) {
    super(http, 'pecas');
  }
}
