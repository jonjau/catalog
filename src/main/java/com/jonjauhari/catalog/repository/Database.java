package com.jonjauhari.catalog.repository;

import java.sql.*;

/**
 * This class manages the connection to the application database
 */
public class Database {
    // storing raw credentials...
    private String url;
    private String username;
    private String password;

    private Connection connection;

    /**
     * @param url      URL to the database
     * @param username username to use when connecting
     * @param password password to use when connecting
     */
    public Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the connection to the application database
     */
    private Connection getConnection() {
        try {
            // reconnect if no current valid connection
            if (connection == null || !connection.isValid(5)) {
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            // impossible
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Convenience function to get a connection and prepare an SQL statement to be executed on it.
     * Don't forget to close the returned prepared statement.
     *
     * @param query      SQL query string to be executed, can contain '?' wildcards
     * @param parameters the parameters to put into the wildcards in the query
     * @return a prepared statement, ready to execute.
     */
    public PreparedStatement prepareQuery(String query, Object... parameters) throws SQLException {
        var conn = getConnection();
        var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }
}
