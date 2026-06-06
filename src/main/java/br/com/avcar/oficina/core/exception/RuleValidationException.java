package br.com.avcar.oficina.core.exception;

public class RuleValidationException extends BaseException {

    public RuleValidationException(String message) {
        super(
                "Violação de Regra de Negócio",
                message,
                "RULE_VALIDATION_ERROR",
                Severity.ERROR
        );
    }
}
