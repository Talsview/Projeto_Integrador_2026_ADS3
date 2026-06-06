package br.com.avcar.oficina.core.exception;

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(
                "Erro de Regra de Negócio",
                message,
                "BUSINESS_ERROR",
                Severity.ERROR
        );
    }
}
