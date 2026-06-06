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
        DatabaseConnectionSingleton singleton = DatabaseConnectionSingleton.getInstance();
        singleton.configure(jdbcUrl, username, password);
        return singleton.isConnected();
    }
}
