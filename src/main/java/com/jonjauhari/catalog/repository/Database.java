package com.jonjauhari.catalog.repository;

import java.sql.*;

// TODO: starting data
public class Database {
    private String url;
    private String username;
    private String password;

    private Connection connection;

    public Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(5)) {
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            // we should never reach this
            throw new IllegalStateException(e.getMessage());
        }
    }

    public PreparedStatement prepare(String query, Object... parameters) throws SQLException {
        var conn = getConnection();
        var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }

}
