import { BaseDTO } from './base.model';

export type TipoCliente = 'PESSOA_FISICA' | 'PESSOA_JURIDICA';

export interface ClienteResumo extends BaseDTO {
  pessoaId?: number;
  tipoCliente?: TipoCliente;
  nome?: string;
  documento?: string;
  telefone?: string;
  email?: string;
}

export interface ClientePessoaFisica extends BaseDTO {
  pessoaId?: number;
  nome: string;
  telefone?: string;
  email?: string;
  endereco?: string;
  cpf: string;
  rg?: string;
  dataNascimento?: string;
}

export interface ClientePessoaJuridica extends BaseDTO {
  pessoaId?: number;
  nome: string;
  telefone?: string;
  email?: string;
  endereco?: string;
  cnpj: string;
  razaoSocial: string;
  nomeFantasia?: string;
  inscricaoEstadual?: string;
}
