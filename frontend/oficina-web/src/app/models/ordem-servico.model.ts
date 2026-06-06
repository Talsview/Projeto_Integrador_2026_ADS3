import { BaseDTO } from './base.model';

export type PrioridadeOrdemServico = 'BAIXA' | 'NORMAL' | 'ALTA' | 'URGENTE';
export type StatusFluxoOrdemServico = 'ORCAMENTO' | 'EXECUCAO' | 'PAGAMENTO' | 'FINALIZADO';

export interface StatusOrdemServico extends BaseDTO {
  nomeStatus?: string;
  ordemFluxo?: number;
  descricao?: string;
}

export interface HistoricoStatusOrdem extends BaseDTO {
  idOrdemServico?: number;
  idStatusOrdemServico?: number;
  nomeStatus?: string;
  ordemFluxo?: number;
  dataStatus?: string;
  observacao?: string;
}

export interface OrdemServicoResumo extends BaseDTO {
  numeroOs?: string;
  idCliente?: number;
  nomeCliente?: string;
  idVeiculo?: number;
  placaVeiculo?: string;
  descricaoVeiculo?: string;
  dataAbertura?: string;
  dataAprovacao?: string;
  dataFinalizacao?: string;
  prioridade?: PrioridadeOrdemServico;
  valorTotal?: number;
  observacao?: string;
  statusAtual?: string;
}

export interface OrdemServico extends OrdemServicoResumo {
  historicoStatus?: HistoricoStatusOrdem[];
  itensServico?: ItemServico[];
}

export interface AlterarStatusOrdemServico {
  novoStatus: StatusFluxoOrdemServico;
  observacao?: string;
}

export interface ItemServico extends BaseDTO {
  idOrdemServico?: number;
  numeroOs?: string;
  idServico?: number;
  nomeServico?: string;
  idColaborador?: number;
  nomeColaborador?: string;
  descricaoExecucao?: string;
  quantidade?: number;
  valorUnitario?: number;
  valorTotal?: number;
  dataInicio?: string;
  dataFim?: string;
  idExecucaoServicoTerceirizado?: number;
  idEmpresaTerceirizada?: number;
  nomeEmpresaTerceirizada?: string;
  dataEnvioTerceirizacao?: string;
  dataRetornoTerceirizacao?: string;
  valorCobradoTerceirizacao?: number;
  observacaoTerceirizacao?: string;
}

export interface TotalRecursivoOrdemServico {
  idOrdemServico?: number;
  total?: number;
}
