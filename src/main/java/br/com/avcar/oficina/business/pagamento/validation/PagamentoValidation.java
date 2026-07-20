package br.com.avcar.oficina.business.pagamento.validation;

import br.com.avcar.oficina.core.validation.GenericDtoValidation;

import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Validações específicas do módulo Pagamento.
 */
@Component
public class PagamentoValidation extends GenericDtoValidation<PagamentoDTO> {

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(PagamentoDTO dto) {
        validateDto(dto);
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(Long id, PagamentoDTO dto) {
        validateId(id);
        validateDto(dto);
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Pagamento.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateIdOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null || idOrdemServico <= 0) {
            throw new RuleValidationException("ID inválido para Ordem de Serviço no pagamento.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateStatus(StatusPagamento statusPagamento) {
        if (statusPagamento == null) {
            throw new RuleValidationException("O status do pagamento é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    private void validateDto(PagamentoDTO dto) {
        if (dto == null) {
            throw new RuleValidationException("Os dados do pagamento são obrigatórios.");
        }
        validateIdOrdemServico(dto.getIdOrdemServico());
        if (dto.getFormaPagamento() == null) {
            throw new RuleValidationException("A forma de pagamento é obrigatória.");
        }
        if (dto.getValorPago() == null || dto.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleValidationException("O valor do pagamento deve ser maior que zero.");
        }
        ValidationUtils.notFuture(dto.getDataPagamento(), "data de pagamento");
        ValidationUtils.maxLength(dto.getObservacao(), 2000, "observação do pagamento");
        if (dto.getStatusPagamento() == StatusPagamento.CANCELADO && dto.getDataPagamento() != null) {
            throw new RuleValidationException("Pagamento cancelado não deve possuir data de pagamento efetivo.");
        }
        if (dto.getStatusPagamento() == StatusPagamento.ESTORNADO && dto.getDataPagamento() != null) {
            throw new RuleValidationException("Pagamento estornado não deve possuir data de pagamento efetivo.");
        }
    }
}
