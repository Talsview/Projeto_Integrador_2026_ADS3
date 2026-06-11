package br.com.avcar.oficina.core.database;

import java.time.LocalDateTime;

/**
 * Resultado da verificação do ambiente local do banco de dados.
 *
 * A classe passou a carregar informações operacionais adicionais para que o
 * padrão Singleton deixe de ser apenas uma demonstração técnica e passe a
 * apoiar diagnóstico real do sistema em computadores diferentes.
 */
public class DatabaseStatusResult {

    private boolean available;
    private long tempoRespostaMs;
    private String mensagem;
    private LocalDateTime dataHoraVerificacao;
    private LocalDateTime dataHoraUltimaMudancaStatus;
    private long verificacoesRealizadas;
    private long falhasConsecutivas;
    private String ultimoErro;
    private boolean cacheUtilizado;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public DatabaseStatusResult(boolean available, long tempoRespostaMs, String mensagem) {
        this.available = available;
        this.tempoRespostaMs = tempoRespostaMs;
        this.mensagem = mensagem;
    }

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public DatabaseStatusResult(DatabaseStatusResult outro) {
        this.available = outro.available;
        this.tempoRespostaMs = outro.tempoRespostaMs;
        this.mensagem = outro.mensagem;
        this.dataHoraVerificacao = outro.dataHoraVerificacao;
        this.dataHoraUltimaMudancaStatus = outro.dataHoraUltimaMudancaStatus;
        this.verificacoesRealizadas = outro.verificacoesRealizadas;
        this.falhasConsecutivas = outro.falhasConsecutivas;
        this.ultimoErro = outro.ultimoErro;
        this.cacheUtilizado = outro.cacheUtilizado;
    }

    /**
     * Função: Retorna o campo available armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Função: Atualiza o campo available neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Função: Retorna o campo tempo resposta ms armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public long getTempoRespostaMs() {
        return tempoRespostaMs;
    }

    /**
     * Função: Atualiza o campo tempo resposta ms neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setTempoRespostaMs(long tempoRespostaMs) {
        this.tempoRespostaMs = tempoRespostaMs;
    }

    /**
     * Função: Retorna o campo mensagem armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Função: Atualiza o campo mensagem neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Função: Retorna o campo data hora verificacao armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public LocalDateTime getDataHoraVerificacao() {
        return dataHoraVerificacao;
    }

    /**
     * Função: Atualiza o campo data hora verificacao neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setDataHoraVerificacao(LocalDateTime dataHoraVerificacao) {
        this.dataHoraVerificacao = dataHoraVerificacao;
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    public LocalDateTime getDataHoraUltimaMudancaStatus() {
        return dataHoraUltimaMudancaStatus;
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    public void setDataHoraUltimaMudancaStatus(LocalDateTime dataHoraUltimaMudancaStatus) {
        this.dataHoraUltimaMudancaStatus = dataHoraUltimaMudancaStatus;
    }

    /**
     * Função: Retorna o campo verificacoes realizadas armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public long getVerificacoesRealizadas() {
        return verificacoesRealizadas;
    }

    /**
     * Função: Atualiza o campo verificacoes realizadas neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setVerificacoesRealizadas(long verificacoesRealizadas) {
        this.verificacoesRealizadas = verificacoesRealizadas;
    }

    /**
     * Função: Retorna o campo falhas consecutivas armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public long getFalhasConsecutivas() {
        return falhasConsecutivas;
    }

    /**
     * Função: Atualiza o campo falhas consecutivas neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setFalhasConsecutivas(long falhasConsecutivas) {
        this.falhasConsecutivas = falhasConsecutivas;
    }

    /**
     * Função: Retorna o campo ultimo erro armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public String getUltimoErro() {
        return ultimoErro;
    }

    /**
     * Função: Atualiza o campo ultimo erro neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setUltimoErro(String ultimoErro) {
        this.ultimoErro = ultimoErro;
    }

    /**
     * Função: Retorna o campo cache utilizado armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public boolean isCacheUtilizado() {
        return cacheUtilizado;
    }

    /**
     * Função: Atualiza o campo cache utilizado neste objeto.
     * Uso no sistema: permite preencher DTOs e resultados retornados pelas camadas de serviço sem
     * expor os atributos diretamente.
     */
    public void setCacheUtilizado(boolean cacheUtilizado) {
        this.cacheUtilizado = cacheUtilizado;
    }
}
