package br.com.avcar.oficina.core.database;

public class DatabaseStatusResult {

    private boolean available;
    private long tempoRespostaMs;
    private String mensagem;

    public DatabaseStatusResult(boolean available, long tempoRespostaMs, String mensagem) {
        this.available = available;
        this.tempoRespostaMs = tempoRespostaMs;
        this.mensagem = mensagem;
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
}
