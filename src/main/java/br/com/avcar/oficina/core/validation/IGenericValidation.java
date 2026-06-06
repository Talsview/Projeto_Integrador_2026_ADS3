package br.com.avcar.oficina.core.validation;

public interface IGenericValidation<E> {

    void validateFields(E entity);

    default void validateFieldsInsert(E entity) {
    }

    default void validateFieldsUpdate(E entity) {
    }

    void validateInsert(E entity);

    void validateUpdate(E entity);

    void validateDelete(Long id);
}
