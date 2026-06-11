package br.com.avcar.oficina.core.exception;

public class RuleValidationException extends BaseException {

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public RuleValidationException(String message) {
        super(
                "Violação de Regra de Negócio",
                message,
                "RULE_VALIDATION_ERROR",
                Severity.ERROR
        );
    }
}
