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
        setUpTables();
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

    private void setUpTables() {
        try (
                var ps1 = prepareQuery("USE catalog");
                var ps2 = prepareQuery(
                        "CREATE TABLE IF NOT EXISTS `exhibition` (\n" +
                                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                                "  `name` VARCHAR(200) NOT NULL,\n" +
                                "  `description` MEDIUMTEXT NOT NULL,\n" +
                                "  PRIMARY KEY (`id`)\n" +
                                ");"
                );
                var ps3 = prepareQuery(
                        "CREATE TABLE IF NOT EXISTS `artifact` (\n" +
                                "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                                "  `name` VARCHAR(200) NOT NULL,\n" +
                                "  `description` MEDIUMTEXT NOT NULL,\n" +
                                "  `exhibitionId` INT DEFAULT NULL,\n" +
                                "  `length` DOUBLE NOT NULL,\n" +
                                "  `width` DOUBLE NOT NULL,\n" +
                                "  `height` DOUBLE NOT NULL,\n" +
                                "  `weight` DOUBLE NOT NULL,\n" +
                                "  PRIMARY KEY (`id`),\n" +
                                "  KEY `exhibitionId_idx` (`exhibitionId`),\n" +
                                "  CONSTRAINT `exhibitionId`\n" +
                                "    FOREIGN KEY (`exhibitionId`)\n" +
                                "    REFERENCES `exhibition` (`id`)\n" +
                                "    ON DELETE SET NULL\n" +
                                "    ON UPDATE CASCADE\n" +
                                ");"
                )
        ) {
            ps1.execute();
            ps2.execute();
            ps3.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
