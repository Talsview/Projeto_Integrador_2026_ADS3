import { BaseDTO } from './base.model';

export type TipoServico = 'INTERNO' | 'TERCEIRIZADO';

export interface Servico extends BaseDTO {
  nomeServico: string;
  descricao?: string;
  prazoGarantiaDias?: number;
  valorBase?: number;
  tipoServico: TipoServico;
  observacaoInterna?: string;
  observacaoTerceirizacao?: string;
  idEmpresaTerceirizadaPadrao?: number;
  nomeEmpresaTerceirizadaPadrao?: string;
}

export interface EmpresaTerceirizada extends BaseDTO {
  nomeEmpresa: string;
  cnpj?: string;
  telefone?: string;
  email?: string;
  endereco?: string;
}
