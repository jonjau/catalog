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
        try (var ps = prepare("DELETE FROM exhibition WHERE id=?", exhibition.getId())) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveExhibition(Exhibition exhibition) {
        if (exhibition.getId() == null) {
            insertExhibition(exhibition);
        } else {
            updateExhibition(exhibition);
        }
    }

    public void updateExhibition(Exhibition exhibition) {
        try (
                var ps1 = prepare(
                        "UPDATE exhibition SET name = ?, description = ? WHERE id = ?",
                        exhibition.getName(), exhibition.getDescription(), exhibition.getId());
        ) {
            ps1.executeUpdate();
            for (var artifact : exhibition.getArtifacts()) {
                try (
                        var ps2 = prepare(
                                "UPDATE artifact SET exhibitionId = ? WHERE artifact.id = ?",
                                exhibition.getId(), artifact.getId())
                ) {
                    ps2.executeUpdate();
                }
            }

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
        try (var ps =
                     prepare("SELECT * FROM exhibition AS ex LEFT JOIN artifact AS ar ON ex.id " +
                             "= ar.exhibitionId ORDER BY ex.id")) {
            ResultSet result = ps.executeQuery();

            List<Exhibition> exhibitions = new ArrayList<>();

            List<Artifact> artifacts = new ArrayList<>();
            Exhibition prevEx = null;
            while (result.next()) {
                long id = result.getLong(1);
                String name = result.getString(2);
                String desc = result.getString(3);
                var currEx = new Exhibition(id, name, desc);

                if (prevEx != null && !currEx.getId().equals(prevEx.getId())) {
                    exhibitions.add(new Exhibition(prevEx.getId(), prevEx.getName(),
                            prevEx.getDescription(),
                            new ArrayList<>(artifacts)));
                    artifacts.clear();
                }

                long arId = result.getLong(4);
                String arName = result.getString(5);
                String arDesc = result.getString(6);
                if (!result.wasNull()) {
                    artifacts.add(new Artifact(arId, arName, arDesc));
                }
                prevEx = currEx;
            }
            if (prevEx != null &&
                    !exhibitions.get(exhibitions.size() - 1).getId().equals(prevEx.getId())) {
                exhibitions.add(new Exhibition(
                        prevEx.getId(), prevEx.getName(),
                        prevEx.getDescription(),
                        artifacts));
            }
            return exhibitions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteArtifact(Artifact artifact) {
        try (var ps = prepare("DELETE FROM artifact WHERE id=?", artifact.getId())) {
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

                long exId = result.getLong("exhibitionId");
                if (!result.wasNull()) {
                    artifact.setLocation(getExhibition(exId));
                }
                artifacts.add(artifact);
            }
            // ResultSet is autoclosed when its PreparedStatement is closed
            return artifacts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveArtifact(Artifact artifact) {
        if (artifact.getId() == null) {
            insertArtifact(artifact);
        } else {
            updateArtifact(artifact);
        }
    }

    public void updateArtifact(Artifact artifact) {
        try (
                var ps = prepare(
                        "UPDATE artifact SET name=?, description=? WHERE id=?",
                        artifact.getName(), artifact.getDescription(), artifact.getId())
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
