package br.com.avcar.oficina.business.ordemservico.enums;

/**
 * Status oficiais do fluxo da Ordem de Serviço.
 *
 * Regra de negócio: toda OS segue o fluxo Orçamento, Execução,
 * Pagamento e Finalizado.
 */
public enum StatusFluxoOrdemServico {
    ORCAMENTO,
    EXECUCAO,
    PAGAMENTO,
    FINALIZADO
}
