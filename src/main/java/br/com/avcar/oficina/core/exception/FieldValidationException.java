package br.com.avcar.oficina.core.exception;

public class FieldValidationException extends BaseException {

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FieldValidationException(String message) {
        super(
                "Erro de Validação de Campo",
                message,
                "FIELD_VALIDATION_ERROR",
                Severity.WARNING
        );
    }
}
