package br.com.avcar.oficina.core.exception;

public class BusinessException extends BaseException {

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public BusinessException(String message) {
        super(
                "Erro de Regra de Negócio",
                message,
                "BUSINESS_ERROR",
                Severity.ERROR
        );
    }
}
