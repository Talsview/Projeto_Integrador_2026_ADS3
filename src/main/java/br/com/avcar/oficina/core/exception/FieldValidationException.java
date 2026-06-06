package br.com.avcar.oficina.core.exception;

public class FieldValidationException extends BaseException {

    public FieldValidationException(String message) {
        super(
                "Erro de Validação de Campo",
                message,
                "FIELD_VALIDATION_ERROR",
                Severity.WARNING
        );
    }
}
