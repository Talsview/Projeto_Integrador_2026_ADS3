import { BaseDTO } from './base.model';

export type StatusGarantia = 'AGUARDANDO_FINALIZACAO_OS' | 'VIGENTE' | 'ACIONADA' | 'ENCERRADA' | 'EXPIRADA';
export type ResponsabilidadeGarantiaPeca = 'FORNECEDOR' | 'OFICINA' | 'AMBOS';

export interface GarantiaPeca extends BaseDTO {
  idItemPeca?: number;
  idOrdemServico?: number;
  idPeca?: number;
  nomePeca?: string;
  codigoNacional?: string;
  idFornecedor?: number;
  nomeFornecedor?: string;
  prazoDias?: number;
  dataInicio?: string;
  dataFim?: string;
  responsabilidade?: ResponsabilidadeGarantiaPeca;
  statusGarantia?: StatusGarantia;
  observacao?: string;
}

export interface GarantiaServico extends BaseDTO {
  idItemServico?: number;
  idOrdemServico?: number;
  idServico?: number;
  nomeServico?: string;
  idColaborador?: number;
  nomeColaboradorResponsavel?: string;
  prazoDias?: number;
  dataInicio?: string;
  dataFim?: string;
  statusGarantia?: StatusGarantia;
  observacao?: string;
}

export interface AcionamentoGarantia {
  observacao?: string;
}
