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

    public void deleteArtifact(Artifact artifact) {
        try {
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

            // more constraints maybe? need exact equality
            String DELETE_QUERY = "DELETE FROM artifact WHERE artifact.id=?";
            var ps = conn.prepareStatement(DELETE_QUERY);
            ps.setLong(1, artifact.getId());
            ps.execute();

            conn.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Artifact getArtifact(long id) {
        var artifacts = getAllArtifacts();
        for (var artifact : artifacts) {
            if (artifact.getId().equals(id)) {
                return artifact;
            }
        }
        return null;
    }

    public List<Artifact> getAllArtifacts() {
        try {
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            String GET_QUERY = "SELECT * FROM artifact";
            var ps = conn.prepareStatement(GET_QUERY);

            ResultSet result = ps.executeQuery();

            List<Artifact> artifacts = new ArrayList<>();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String desc = result.getString("description");
                var artifact = new Artifact(id, name, desc);
                artifacts.add(artifact);
            }
            conn.close();
            ps.close();

            return artifacts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertArtifact(Artifact artifact) {
        try {
            // try-with-resource statement will auto close the connection.
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            var ps = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, artifact.getName());
            ps.setString(2, artifact.getDescription());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    artifact.setId(rs.getLong(1));
                }
            }
            conn.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
