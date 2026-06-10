package br.com.avcar.oficina.core.designpattern.singleton;

import br.com.avcar.oficina.core.database.DatabaseStatusResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * PADRÃO DE PROJETO: SINGLETON
 *
 * Aplicação reformulada no sistema: monitor único do ambiente local de banco de
 * dados. Além de abrir uma conexão de diagnóstico com o PostgreSQL, o Singleton
 * mantém o último estado verificado, a quantidade de verificações, falhas
 * consecutivas, horário da última mudança de status e um pequeno cache de
 * leitura para evitar tentativas repetidas de conexão em poucos milissegundos.
 *
 * Justificativa funcional: a oficina deve funcionar localmente, inclusive em
 * computadores diferentes. Com esse Singleton, a tela de Configurações consegue
 * informar não apenas se o banco respondeu, mas também se há instabilidade,
 * falhas repetidas ou uso de resultado em cache.
 */
public final class DatabaseConnectionSingleton {

    private static final long CACHE_VALIDO_MS = 1500L;
    private static volatile DatabaseConnectionSingleton instance;

    private String jdbcUrl;
    private String username;
    private String password;

    private DatabaseStatusResult ultimoResultado;
    private long instanteUltimaVerificacaoMs;
    private long verificacoesRealizadas;
    private long falhasConsecutivas;
    private Boolean ultimoStatusDisponivel;
    private LocalDateTime dataHoraUltimaMudancaStatus;

    private DatabaseConnectionSingleton() {
    }

    public static DatabaseConnectionSingleton getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionSingleton.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionSingleton();
                }
            }
        }
        return instance;
    }

    public synchronized void configure(String jdbcUrl, String username, String password) {
        boolean configuracaoAlterada = !Objects.equals(this.jdbcUrl, jdbcUrl)
                || !Objects.equals(this.username, username)
                || !Objects.equals(this.password, password);

        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;

        if (configuracaoAlterada) {
            this.ultimoResultado = null;
            this.instanteUltimaVerificacaoMs = 0L;
            this.falhasConsecutivas = 0L;
            this.ultimoStatusDisponivel = null;
            this.dataHoraUltimaMudancaStatus = null;
        }
    }

    public synchronized boolean isConnected() {
        return checkStatus().isAvailable();
    }

    /**
     * Verifica a conexão e retorna um diagnóstico operacional. A conexão é
     * fechada imediatamente após a verificação para não competir com o pool JPA.
     */
    public synchronized DatabaseStatusResult checkStatus() {
        long agoraMs = System.currentTimeMillis();
        if (ultimoResultado != null && agoraMs - instanteUltimaVerificacaoMs <= CACHE_VALIDO_MS) {
            DatabaseStatusResult copia = new DatabaseStatusResult(ultimoResultado);
            copia.setCacheUtilizado(true);
            return copia;
        }

        long inicio = System.currentTimeMillis();
        verificacoesRealizadas++;

        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return registrarResultado(false, 0L, "URL JDBC do PostgreSQL não configurada.", "spring.datasource.url vazio ou ausente");
        }

        DriverManager.setLoginTimeout(2);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            boolean conectado = connection.isValid(2);
            long tempoRespostaMs = System.currentTimeMillis() - inicio;
            String mensagem = conectado
                    ? "Backend e PostgreSQL local disponíveis."
                    : "Backend disponível, porém o PostgreSQL local não validou a conexão.";
            return registrarResultado(conectado, tempoRespostaMs, mensagem, conectado ? null : "Conexão inválida retornada pelo driver JDBC.");
        } catch (SQLException exception) {
            long tempoRespostaMs = System.currentTimeMillis() - inicio;
            return registrarResultado(false, tempoRespostaMs,
                    "Backend disponível, porém a conexão com o PostgreSQL local falhou.",
                    sanitizarErro(exception.getMessage()));
        }
    }

    private DatabaseStatusResult registrarResultado(boolean disponivel, long tempoRespostaMs, String mensagem, String erro) {
        LocalDateTime agora = LocalDateTime.now();

        if (ultimoStatusDisponivel == null || !ultimoStatusDisponivel.equals(disponivel)) {
            ultimoStatusDisponivel = disponivel;
            dataHoraUltimaMudancaStatus = agora;
        }

        falhasConsecutivas = disponivel ? 0L : falhasConsecutivas + 1L;

        DatabaseStatusResult resultado = new DatabaseStatusResult(disponivel, tempoRespostaMs, mensagem);
        resultado.setDataHoraVerificacao(agora);
        resultado.setDataHoraUltimaMudancaStatus(dataHoraUltimaMudancaStatus);
        resultado.setVerificacoesRealizadas(verificacoesRealizadas);
        resultado.setFalhasConsecutivas(falhasConsecutivas);
        resultado.setUltimoErro(erro);
        resultado.setCacheUtilizado(false);

        this.ultimoResultado = new DatabaseStatusResult(resultado);
        this.instanteUltimaVerificacaoMs = System.currentTimeMillis();
        return resultado;
    }

    private String sanitizarErro(String mensagemErro) {
        if (mensagemErro == null || mensagemErro.isBlank()) {
            return "Erro JDBC não detalhado.";
        }
        return mensagemErro.replaceAll("(?i)password=[^\\s;]+", "password=***");
    }
}
