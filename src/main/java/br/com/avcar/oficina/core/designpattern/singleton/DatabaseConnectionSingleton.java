package br.com.avcar.oficina.core.designpattern.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PADRÃO DE PROJETO: SINGLETON
 *
 * Aplicação no sistema: ponto único para verificação de conexão local com o
 * PostgreSQL. O sistema utiliza Spring Data JPA para persistência, mas esta
 * classe demonstra o padrão Singleton conforme exigência da disciplina e pode
 * ser usada para diagnóstico da instalação local do banco.
 *
 * Justificativa: como a oficina deve funcionar localmente, é importante existir
 * uma forma centralizada de validar se a conexão com o banco está disponível.
 */
public final class DatabaseConnectionSingleton {

    private static volatile DatabaseConnectionSingleton instance;

    private String jdbcUrl;
    private String username;
    private String password;

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
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * Verifica a conexão de forma rápida e sem manter uma conexão JDBC aberta.
     * Isso evita lentidão na tela Angular quando o banco está indisponível ou
     * quando uma conexão antiga fica inválida.
     */
    public synchronized boolean isConnected() {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return false;
        }

        DriverManager.setLoginTimeout(2);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            return connection.isValid(2);
        } catch (SQLException exception) {
            return false;
        }
    }
}
