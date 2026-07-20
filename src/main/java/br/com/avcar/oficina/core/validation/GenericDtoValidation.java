package br.com.avcar.oficina.core.validation;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.exception.FieldValidationException;

/**
 * Classe base genérica para validações da camada de negócio baseadas em DTO.
 *
 * <p>D representa o DTO recebido pela Controller e validado antes de chegar ao banco.
 * A classe oferece pontos comuns para validação de identificador e nulidade, enquanto
 * cada validação concreta mantém as regras específicas de seu módulo.</p>
 */
public abstract class GenericDtoValidation<D extends BaseDTO> implements IGenericDtoValidation<D> {

    @Override
    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador é obrigatório.");
        }
    }

    protected void validateDtoNotNull(D dto, String nomeModulo) {
        if (dto == null) {
            throw new FieldValidationException("Os dados de " + nomeModulo + " são obrigatórios.");
        }
    }
}
