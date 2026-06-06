package br.com.avcar.oficina.business.pagamento.validation;

import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Validações específicas do módulo Pagamento.
 */
@Component
public class PagamentoValidation {

    public void validateInsert(PagamentoDTO dto) {
        validateDto(dto);
    }

    public void validateUpdate(Long id, PagamentoDTO dto) {
        validateId(id);
        validateDto(dto);
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Pagamento.");
        }
    }

    public void validateIdOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null || idOrdemServico <= 0) {
            throw new RuleValidationException("ID inválido para Ordem de Serviço no pagamento.");
        }
    }

    public void validateStatus(StatusPagamento statusPagamento) {
        if (statusPagamento == null) {
            throw new RuleValidationException("O status do pagamento é obrigatório.");
        }
    }

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
        if (dto.getStatusPagamento() == StatusPagamento.CANCELADO && dto.getDataPagamento() != null) {
            throw new RuleValidationException("Pagamento cancelado não deve possuir data de pagamento efetivo.");
        }
    }
}
