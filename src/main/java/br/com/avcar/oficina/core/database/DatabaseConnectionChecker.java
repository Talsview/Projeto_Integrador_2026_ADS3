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

    /**
     * Função: Retorna o campo database available armazenado neste objeto.
     * Uso no sistema: disponibiliza esse dado para respostas da API, telas, diagnósticos ou regras que
     * precisam consultar o estado atual.
     */
    public boolean isDatabaseAvailable() {
        return checkStatus().isAvailable();
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    public DatabaseStatusResult checkStatus() {
        DatabaseConnectionSingleton singleton = DatabaseConnectionSingleton.getInstance();
        singleton.configure(jdbcUrl, username, password);
        return singleton.checkStatus();
    }
}
