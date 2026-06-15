import { BaseDTO } from './base.model';

export interface HistoricoProprietario extends BaseDTO {
  clienteId?: number;
  nomeCliente?: string;
  veiculoId?: number;
  placaVeiculo?: string;
  nomeMarcaVeiculo?: string;
  nomeModeloVeiculo?: string;
  anoVeiculo?: number;
  anoModelo?: number;
  chassiVeiculo?: string;
  corVeiculo?: string;
  quilometragemAtual?: number;
  dataInicioPosse?: string;
  dataFimPosse?: string;
  proprietarioAtual?: boolean;
  observacao?: string;
}

export interface TransferenciaProprietario {
  novoClienteId?: number;
  dataInicioPosse?: string;
  observacao?: string;
}

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

export interface VeiculoDetalhe extends VeiculoResumo {
  observacao?: string;
  dataInicioPosse?: string;
  observacaoPosse?: string;
  historicoProprietarios?: HistoricoProprietario[];
}

export interface Marca extends BaseDTO {
  nomeMarca: string;
}

export interface Modelo extends BaseDTO {
  marcaId: number;
  nomeMarca?: string;
  nomeModelo: string;
}
