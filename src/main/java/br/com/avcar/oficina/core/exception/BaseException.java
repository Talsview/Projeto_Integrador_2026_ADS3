package br.com.avcar.oficina.core.exception;

public abstract class BaseException extends RuntimeException {

    private final String title;
    private final String motive;
    private final Severity severity;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    protected BaseException(String title, String message, String motive, Severity severity) {
        super(message);
        this.title = title;
        this.motive = motive;
        this.severity = severity;
    }

    /**
     * Função: Retorna o campo title armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Função: Retorna o campo motive armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public String getMotive() {
        return motive;
    }

    /**
     * Função: Retorna o campo severity armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public Severity getSeverity() {
        return severity;
    }
}
