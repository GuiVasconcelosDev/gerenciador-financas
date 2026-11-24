package br.com.guilhermedev.financas.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() {
        String url = requiredEnvironmentVariable("DB_URL");
        String user = requiredEnvironmentVariable("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (password == null) {
            throw new IllegalStateException("A variável de ambiente DB_PASSWORD não foi definida.");
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível conectar ao banco de dados PostgreSQL.", e);
        }
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("A variável de ambiente " + name + " não foi definida.");
        }
        return value;
    }
}
