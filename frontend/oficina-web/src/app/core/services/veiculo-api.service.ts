import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, timeout } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { BaseApiService } from './base-api.service';
import { HistoricoProprietario, TransferenciaProprietario, VeiculoDetalhe, VeiculoResumo } from '../../models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class VeiculoApiService extends BaseApiService<VeiculoResumo> {
  /**
   * Função: recebe o HttpClient usado nas chamadas REST do módulo de veículos.
   * Uso no sistema: permite que a tela consulte veículos, histórico de proprietários e transferências
   * sem montar URLs diretamente dentro dos componentes.
   */
  constructor(http: HttpClient) {
    super(http, 'veiculos');
  }

  /**
   * Função: busca a versão detalhada do veículo, incluindo proprietário atual e histórico de posse.
   * Uso no sistema: alimenta a aba Histórico de Proprietários com os dados completos de rastreabilidade.
   */
  buscarDetalhe(id: number): Observable<VeiculoDetalhe> {
    return this.http.get<ApiResponse<VeiculoDetalhe> | VeiculoDetalhe>(
      `${this.apiBaseUrl}/veiculos/${id}`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => this.extrairDados(response) as VeiculoDetalhe)
    );
  }


  /**
   * Função: consulta o histórico consolidado de todos os proprietários.
   * Uso no sistema: permite montar a tela por cliente, sem duplicar nomes e sem esconder
   * proprietários anteriores depois de uma transferência de posse.
   */
  listarHistoricoProprietariosConsolidado(): Observable<HistoricoProprietario[]> {
    return this.http.get<ApiResponse<HistoricoProprietario[]> | HistoricoProprietario[]>(
      `${this.apiBaseUrl}/veiculos/historico-proprietarios`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => [...((this.extrairDados(response) as HistoricoProprietario[]) ?? [])])
    );
  }

  /**
   * Função: consulta somente o histórico de proprietários de um veículo.
   * Uso no sistema: permite atualizar a listagem histórica sem recarregar todos os cadastros da tela.
   */
  listarHistoricoProprietarios(veiculoId: number): Observable<HistoricoProprietario[]> {
    return this.http.get<ApiResponse<HistoricoProprietario[]> | HistoricoProprietario[]>(
      `${this.apiBaseUrl}/veiculos/${veiculoId}/historico-proprietarios`,
      this.opcoesSemCache()
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => [...((this.extrairDados(response) as HistoricoProprietario[]) ?? [])])
    );
  }

  /**
   * Função: registra uma transferência de proprietário para o veículo informado.
   * Uso no sistema: encerra a posse anterior e cria um novo histórico, preservando o dono antigo.
   */
  transferirProprietario(veiculoId: number, payload: TransferenciaProprietario): Observable<VeiculoDetalhe> {
    return this.http.patch<ApiResponse<VeiculoDetalhe> | VeiculoDetalhe>(
      `${this.apiBaseUrl}/veiculos/${veiculoId}/transferir-proprietario`,
      payload
    ).pipe(
      timeout(this.tempoLimiteMs),
      map(response => this.extrairDados(response) as VeiculoDetalhe)
    );
  }
}
