package com.jonjauhari.catalog;

import com.jonjauhari.catalog.model.Artifact;

import java.sql.*;

public class Database {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/catalog?useSSL=false";
    private static final String DB_USER = "catalog";
    private static final String DB_PASSWORD = "catalog";
    private static final String INSERT_QUERY = "INSERT INTO artifact (name, description) " +
            "VALUES (?, ?)";

    public void insertArtifact(Artifact artifact) {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                DB_PASSWORD);
             // Step 2:Create a statement using connection object
             PreparedStatement ps = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, artifact.getName());
            ps.setString(2, artifact.getDescription());

            System.out.println(ps);
            // Step 3: Execute the query or update query
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    artifact.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
