import { BaseDTO } from './base.model';

export type FormaPagamento = 'DINHEIRO' | 'PIX' | 'CARTAO_DEBITO' | 'CARTAO_CREDITO' | 'TRANSFERENCIA' | 'BOLETO' | 'OUTRO';
export type StatusPagamento = 'PENDENTE' | 'PAGO' | 'CANCELADO' | 'ESTORNADO';

export interface Pagamento extends BaseDTO {
  idOrdemServico?: number;
  numeroOs?: string;
  formaPagamento?: FormaPagamento;
  valorPago?: number;
  dataPagamento?: string;
  statusPagamento?: StatusPagamento;
  observacao?: string;
}

export interface ResumoPagamentoOrdemServico {
  idOrdemServico?: number;
  numeroOs?: string;
  valorTotalOrdemServico?: number;
  valorPago?: number;
  valorPendente?: number;
  quitada?: boolean;
}
