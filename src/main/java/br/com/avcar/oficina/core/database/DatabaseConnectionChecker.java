package br.com.avcar.oficina.core.database;

import br.com.avcar.oficina.core.designpattern.singleton.DatabaseConnectionSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Componente de apoio para verificar a conexão local com o PostgreSQL.
 */
@Component
public class DatabaseConnectionChecker {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public boolean isDatabaseAvailable() {
        return checkStatus().isAvailable();
    }

    public DatabaseStatusResult checkStatus() {
        long inicio = System.currentTimeMillis();

        DatabaseConnectionSingleton singleton = DatabaseConnectionSingleton.getInstance();
        singleton.configure(jdbcUrl, username, password);

        boolean available = singleton.isConnected();
        long tempoRespostaMs = System.currentTimeMillis() - inicio;

        String mensagem = available
                ? "Backend e PostgreSQL local disponíveis."
                : "Backend disponível, porém a conexão com o PostgreSQL local falhou.";

        return new DatabaseStatusResult(available, tempoRespostaMs, mensagem);
    }
}
