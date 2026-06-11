import { BaseDTO } from './base.model';

export interface Fornecedor extends BaseDTO {
  nomeFornecedor: string;
  cnpj?: string;
  telefone?: string;
  email?: string;
  endereco?: string;
}

export interface Peca extends BaseDTO {
  nomePeca: string;
  codigoNacional?: string;
  marcaPeca?: string;
  modeloAplicavel?: string;
  anoVeiculo?: number;
  anoModelo?: number;
  idFornecedorPadrao?: number;
  nomeFornecedorPadrao?: string;
  valorUnitarioPadrao?: number;
  prazoGarantiaDias?: number;
  descricao?: string;
}

export interface ItemPeca extends BaseDTO {
  idOrdemServico?: number;
  idPeca?: number;
  nomePeca?: string;
  codigoNacional?: string;
  idFornecedor?: number;
  nomeFornecedor?: string;
  quantidade?: number;
  valorUnitario?: number;
  valorTotal?: number;
  observacao?: string;
}
