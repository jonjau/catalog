package com.jonjauhari.catalog.repository;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for controlling access to Artifacts in the database
 */
public class ArtifactRepository implements Repository<Artifact, Long> {

    private Database db;
    private ExhibitionRepository exRepo;

    /**
     * @param db database connection for this repo
     * @param exRepo exhibition repo for searching up artifact's locations
     */
    public ArtifactRepository(Database db, ExhibitionRepository exRepo) {
        this.db = db;
        this.exRepo = exRepo;
    }

    @Override
    public void save(Artifact artifact) {
        if (artifact.getId() == null) {
            insertArtifact(artifact);
        } else {
            updateArtifact(artifact);
        }
    }

    @Override
    public Artifact findById(Long id) {
        var artifacts = findAll();
        for (var artifact : artifacts) {
            if (artifact.getId().equals(id)) {
                return artifact;
            }
        }
        return null;
    }

    @Override
    public List<Artifact> findAll() {
        try (var ps = db.prepareQuery("SELECT * FROM artifact")) {
            ResultSet result = ps.executeQuery();
            List<Artifact> artifacts = new ArrayList<>();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String desc = result.getString("description");
                double length = result.getDouble("length");
                double width = result.getDouble("width");
                double height = result.getDouble("height");
                double weight = result.getDouble("weight");

                var artifact = new Artifact(id, name, desc, new Dimensions(length, width, height),
                        weight);

                long exId = result.getLong("exhibitionId");
                if (!result.wasNull()) {
                    // if artifact has a location, look it up then set it
                    artifact.setLocation(exRepo.findById(exId));
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

    @Override
    public void delete(Artifact artifact){
        try (var ps = db.prepareQuery("DELETE FROM artifact WHERE id=?", artifact.getId())) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param artifact artifact to update
     */
    private void updateArtifact(Artifact artifact) {
        var dimensions = artifact.getDimensions();
        try (
                var ps = db.prepareQuery(
                        "UPDATE artifact SET name=?, description=?, length=?, width=?, " +
                                "height=?, weight=? WHERE id=?",
                        artifact.getName(), artifact.getDescription(),
                        dimensions.length, dimensions.width, dimensions.height,
                        artifact.getWeight(), artifact.getId())
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert an artifact that doesn't have an ID into the database, the artifact's ID will be set
     * by this function
     * @param artifact new artifact to insert
     */
    private void insertArtifact(Artifact artifact) {
        var dimensions = artifact.getDimensions();
        try (
                var ps = db.prepareQuery(
                        "INSERT INTO artifact (name, description, length, width, height, weight) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        artifact.getName(), artifact.getDescription(), dimensions.length,
                        dimensions.width, dimensions.height, artifact.getWeight())
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
