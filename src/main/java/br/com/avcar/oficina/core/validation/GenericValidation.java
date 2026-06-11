package br.com.avcar.oficina.core.validation;

import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.model.BaseModel;

/**
 * Classe base para validações dos módulos de negócio.
 *
 * As validações específicas devem ser implementadas nas subclasses de cada
 * módulo, mantendo as regras de domínio fora da Controller e fora da View.
 */
public abstract class GenericValidation<E extends BaseModel> implements IGenericValidation<E> {

    @Override
    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateInsert(E entity) {
        validateEntityNotNull(entity);

        if (entity.getId() != null) {
            throw new FieldValidationException("O ID deve ser nulo para cadastrar um novo registro.");
        }

        validateFields(entity);
        validateFieldsInsert(entity);
    }

    @Override
    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateUpdate(E entity) {
        validateEntityNotNull(entity);

        if (entity.getId() == null) {
            throw new FieldValidationException("O ID é obrigatório para atualizar o registro.");
        }

        validateFields(entity);
        validateFieldsUpdate(entity);
    }

    @Override
    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    public void validateDelete(Long id) {
        if (id == null) {
            throw new FieldValidationException("O ID é obrigatório para excluir o registro.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    protected void validateEntityNotNull(E entity) {
        if (entity == null) {
            throw new FieldValidationException("Os dados do registro são obrigatórios.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    protected void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    protected void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new FieldValidationException("O campo " + fieldName + " é obrigatório.");
        }
    }

    /**
     * Função: Confere se o identificador foi informado e se possui valor válido antes da consulta ou
     * alteração.
     * Uso no sistema: impede que dados incompletos ou inconsistentes avancem para a camada de serviço
     * e banco de dados.
     */
    protected void validatePositive(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new FieldValidationException("O campo " + fieldName + " deve ser maior que zero.");
        }
    }
}
