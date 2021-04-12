package com.jonjauhari.catalog;

import com.jonjauhari.catalog.model.Artifact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/catalog?useSSL=false";
    private static final String DB_USER = "catalog";
    private static final String DB_PASSWORD = "catalog";
    private static final String INSERT_QUERY = "INSERT INTO artifact (name, description) " +
            "VALUES (?, ?)";

    private String url = DB_CONNECTION;
    private String user = DB_USER;
    private String password = DB_PASSWORD;

    private Connection connection;

    private Connection getConnection() {
        try {
            if (connection != null && connection.isValid(5)) {
                return connection;
            } else {
                return DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            // we should never reach this
            throw new IllegalStateException(e.getMessage());
        }
    }

    private void execute(String query, Object... parameters) throws SQLException {
        var conn = getConnection();
        var ps = conn.prepareStatement(query);
        ps.executeQuery();
        ps.close();
    }

    public List<Artifact> getAllArtifacts() {
        try {
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            String GET_QUERY = "SELECT * FROM artifact";
            var ps = conn.prepareStatement(GET_QUERY);

            System.out.println(ps);
            // Step 3: Execute the query or update query
            ResultSet result = ps.executeQuery();

            List<Artifact> artifacts = new ArrayList<>();
            while (result.next()) {
                String name = result.getString("name");
                String desc = result.getString("description");
                var artifact = new Artifact(name, desc);
                artifacts.add(artifact);
            }
            return artifacts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertArtifact(Artifact artifact) {
        try {
            // Step 1: Establishing a Connection and
            // try-with-resource statement will auto close the connection.
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            var ps = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
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
            e.printStackTrace();
        }
    }
}
