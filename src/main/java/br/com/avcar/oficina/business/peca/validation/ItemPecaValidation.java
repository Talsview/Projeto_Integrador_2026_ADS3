package br.com.avcar.oficina.business.peca.validation;

import br.com.avcar.oficina.business.peca.dto.ItemPecaDTO;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do registro de ItemPeca.
 */
@Component
public class ItemPecaValidation {

    public void validateInsert(ItemPecaDTO dto) {
        validateFields(dto);
    }

    public void validateUpdate(Long id, ItemPecaDTO dto) {
        validateId(id);
        validateFields(dto);
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do item de peça é obrigatório.");
        }
    }

    public void validateIdOrdemServico(Long idOrdemServico) {
        if (idOrdemServico == null || idOrdemServico <= 0) {
            throw new FieldValidationException("O identificador da ordem de serviço é obrigatório.");
        }
    }

    private void validateFields(ItemPecaDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do item de peça são obrigatórios.");
        }
        validateIdOrdemServico(dto.getIdOrdemServico());
        if (dto.getIdPeca() == null || dto.getIdPeca() <= 0) {
            throw new FieldValidationException("A peça aplicada é obrigatória.");
        }
        if (dto.getIdFornecedor() == null || dto.getIdFornecedor() <= 0) {
            throw new FieldValidationException("O fornecedor da peça aplicada é obrigatório.");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FieldValidationException("A quantidade da peça deve ser maior que zero.");
        }
        if (dto.getValorUnitario() == null || dto.getValorUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new FieldValidationException("O valor unitário da peça não pode ser negativo.");
        }
    }
}
