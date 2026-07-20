package br.com.avcar.oficina.core.validation;

import br.com.avcar.oficina.core.dto.BaseDTO;

/**
 * Contrato genérico para validações baseadas em DTOs.
 *
 * <p>As validações do sistema recebem dados da API em forma de DTO. Este contrato
 * explicita essa camada dentro da arquitetura monolítica em camadas, permitindo que
 * cada validação informe o tipo de DTO que valida.</p>
 */
public interface IGenericDtoValidation<D extends BaseDTO> {

    default void validateInsert(D dto) {
    }

    default void validateUpdate(Long id, D dto) {
    }

    default void validateId(Long id) {
    }
}
