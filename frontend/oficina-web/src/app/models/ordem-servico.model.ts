import { BaseDTO } from './base.model';

export type PrioridadeOrdemServico = 'BAIXA' | 'NORMAL' | 'ALTA' | 'URGENTE';
export type StatusFluxoOrdemServico = 'ORCAMENTO' | 'EXECUCAO' | 'PAGAMENTO' | 'FINALIZADO';

export interface OrdemServicoResumo extends BaseDTO {
  numeroOs?: string;
  idCliente?: number;
  nomeCliente?: string;
  idVeiculo?: number;
  placaVeiculo?: string;
  descricaoVeiculo?: string;
  dataAbertura?: string;
  dataFinalizacao?: string;
  prioridade?: PrioridadeOrdemServico;
  valorTotal?: number;
  statusAtual?: string;
}

export interface AlterarStatusOrdemServico {
  novoStatus: StatusFluxoOrdemServico;
  observacao?: string;
}

export interface TotalRecursivoOrdemServico {
  idOrdemServico?: number;
  total?: number;
}
