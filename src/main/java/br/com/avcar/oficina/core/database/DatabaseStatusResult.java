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

    public DatabaseStatusResult(boolean available, long tempoRespostaMs, String mensagem) {
        this.available = available;
        this.tempoRespostaMs = tempoRespostaMs;
        this.mensagem = mensagem;
    }

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getTempoRespostaMs() {
        return tempoRespostaMs;
    }

    public void setTempoRespostaMs(long tempoRespostaMs) {
        this.tempoRespostaMs = tempoRespostaMs;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHoraVerificacao() {
        return dataHoraVerificacao;
    }

    public void setDataHoraVerificacao(LocalDateTime dataHoraVerificacao) {
        this.dataHoraVerificacao = dataHoraVerificacao;
    }

    public LocalDateTime getDataHoraUltimaMudancaStatus() {
        return dataHoraUltimaMudancaStatus;
    }

    public void setDataHoraUltimaMudancaStatus(LocalDateTime dataHoraUltimaMudancaStatus) {
        this.dataHoraUltimaMudancaStatus = dataHoraUltimaMudancaStatus;
    }

    public long getVerificacoesRealizadas() {
        return verificacoesRealizadas;
    }

    public void setVerificacoesRealizadas(long verificacoesRealizadas) {
        this.verificacoesRealizadas = verificacoesRealizadas;
    }

    public long getFalhasConsecutivas() {
        return falhasConsecutivas;
    }

    public void setFalhasConsecutivas(long falhasConsecutivas) {
        this.falhasConsecutivas = falhasConsecutivas;
    }

    public String getUltimoErro() {
        return ultimoErro;
    }

    public void setUltimoErro(String ultimoErro) {
        this.ultimoErro = ultimoErro;
    }

    public boolean isCacheUtilizado() {
        return cacheUtilizado;
    }

    public void setCacheUtilizado(boolean cacheUtilizado) {
        this.cacheUtilizado = cacheUtilizado;
    }
}
