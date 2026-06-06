import { BaseDTO } from './base.model';

export interface VeiculoResumo extends BaseDTO {
  placa?: string;
  chassi?: string;
  cor?: string;
  anoVeiculo?: number;
  anoModelo?: number;
  quilometragemAtual?: number;
  marcaId?: number;
  nomeMarca?: string;
  modeloId?: number;
  nomeModelo?: string;
  proprietarioAtualId?: number;
  nomeProprietarioAtual?: string;
}

export interface Marca extends BaseDTO {
  nomeMarca: string;
}

export interface Modelo extends BaseDTO {
  marcaId: number;
  nomeMarca?: string;
  nomeModelo: string;
}
