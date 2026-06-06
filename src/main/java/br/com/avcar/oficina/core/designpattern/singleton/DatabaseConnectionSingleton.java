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

    private Connection connection;
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

    public synchronized Connection getConnection() throws SQLException {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new SQLException("A URL JDBC não foi configurada.");
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        }

        return connection;
    }

    public synchronized boolean isConnected() {
        try {
            return getConnection() != null && !getConnection().isClosed();
        } catch (SQLException exception) {
            return false;
        }
    }

    public synchronized void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
