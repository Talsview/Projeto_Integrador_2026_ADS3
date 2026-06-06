package br.com.avcar.oficina.business.garantia.validation;

import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Validações comuns para garantias de peças e serviços.
 */
@Component
public class GarantiaValidation {

    public void validateId(Long id, String entidade) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador de " + entidade + " é obrigatório.");
        }
    }

    public void validateIdOrdemServico(Long idOrdemServico) {
        validateId(idOrdemServico, "Ordem de Serviço");
    }
}
