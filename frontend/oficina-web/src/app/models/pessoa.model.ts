import { BaseDTO } from './base.model';

export type StatusColaborador = 'ATIVO' | 'AFASTADO' | 'DESLIGADO';

export interface Funcao extends BaseDTO {
  nomeFuncao: string;
  descricao?: string;
}

export interface ColaboradorFuncao extends BaseDTO {
  colaboradorId?: number;
  funcaoId?: number;
  nomeFuncao?: string;
  dataInicio?: string;
  dataFim?: string;
}

export interface Colaborador extends BaseDTO {
  pessoaId?: number;
  nome: string;
  telefone?: string;
  email?: string;
  endereco?: string;
  dataAdmissao?: string;
  statusColaborador?: StatusColaborador;
  funcoesIds?: number[];
  funcoes?: ColaboradorFuncao[];
}

export interface ColaboradorResumo extends BaseDTO {
  pessoaId?: number;
  nome?: string;
  telefone?: string;
  email?: string;
  dataAdmissao?: string;
  statusColaborador?: StatusColaborador;
  funcoes?: string;
}
