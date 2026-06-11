package br.com.avcar.oficina.business.garantia.validation;

import br.com.avcar.oficina.core.exception.FieldValidationException;
import org.springframework.stereotype.Component;

/**
 * Validações comuns para garantias de peças e serviços.
 */
@Component
public class GarantiaValidation {

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateId(Long id, String entidade) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador de " + entidade + " é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateIdOrdemServico(Long idOrdemServico) {
        validateId(idOrdemServico, "Ordem de Serviço");
    }
}
