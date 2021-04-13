package com.jonjauhari.catalog;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Exhibition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO: intialise with starting data, abstract into repositories
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

    private PreparedStatement prepare(String query, Object... parameters) throws SQLException {
        var conn = getConnection();
        var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }

    public void deleteExhibition(Exhibition exhibition) {
        try (var ps = prepare("DELETE FROM exhibition WHERE exhibition.id=?", exhibition.getId())) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertExhibition(Exhibition exhibition) {
        try (
                var ps = prepare(
                        "INSERT INTO exhibition (name, description) VALUES (?, ?)",
                        exhibition.getName(), exhibition.getDescription())
        ) {
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                exhibition.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Exhibition getExhibition(long id) {
        var exhibitions = getAllExhibitions();
        for (var exhibition : exhibitions) {
            if (exhibition.getId().equals(id)) {
                return exhibition;
            }
        }
        return null;
    }

    public List<Exhibition> getAllExhibitions() {
        try (var ps = prepare("SELECT * FROM exhibition")) {
            ResultSet result = ps.executeQuery();
            List<Exhibition> exhibitions = new ArrayList<>();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String desc = result.getString("description");

                var exhibition = new Exhibition(id, name, desc);
                exhibitions.add(exhibition);
            }
            return exhibitions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteArtifact(Artifact artifact) {
        try (var ps = prepare("DELETE FROM artifact WHERE artifact.id=?", artifact.getId())) {
            ps.execute();
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
        try (var ps = prepare("SELECT * FROM artifact")) {
            ResultSet result = ps.executeQuery();
            List<Artifact> artifacts = new ArrayList<>();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String desc = result.getString("description");

                var artifact = new Artifact(id, name, desc);
                artifacts.add(artifact);
            }
            // ResultSet is autoclosed when its PreparedStatement is closed
            return artifacts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertArtifact(Artifact artifact) {
        try (
                var ps = prepare(
                        "INSERT INTO artifact (name, description) VALUES (?, ?)",
                        artifact.getName(), artifact.getDescription())
        ) {
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                artifact.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
