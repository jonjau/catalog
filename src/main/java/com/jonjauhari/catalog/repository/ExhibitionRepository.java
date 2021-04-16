package com.jonjauhari.catalog.repository;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import com.jonjauhari.catalog.model.Exhibition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for controlling access to Exhibition in the database
 */
public class ExhibitionRepository implements Repository<Exhibition, Long> {

    private Database db;

    /**
     * @param db database connection for this repo
     */
    public ExhibitionRepository(Database db) {
        this.db = db;
    }

    @Override
    public void save(Exhibition exhibition) {
        if (exhibition.getId() == null) {
            insertExhibition(exhibition);
        } else {
            updateExhibition(exhibition);
        }
    }

    @Override
    public Exhibition findById(Long id) {
        var exhibitions = findAll();
        for (var exhibition : exhibitions) {
            if (exhibition.getId().equals(id)) {
                return exhibition;
            }
        }
        return null;
    }

    @Override
    public List<Exhibition> findAll() {
        try (var ps =
                     db.prepareQuery("SELECT * FROM exhibition AS ex LEFT JOIN artifact AS ar ON " +
                             "ex.id = ar.exhibitionId ORDER BY ex.id")) {
            ResultSet result = ps.executeQuery();
            List<Exhibition> exhibitions = new ArrayList<>();

            // manually aggregate each exhibition's artifacts into lists, sliding window style
            List<Artifact> artifacts = new ArrayList<>();
            Exhibition prevEx = null;
            while (result.next()) {
                long id = result.getLong(1);
                String name = result.getString(2);
                String desc = result.getString(3);
                var currEx = new Exhibition(id, name, desc);

                if (prevEx != null && !currEx.getId().equals(prevEx.getId())) {
                    // moving on to new exhibition, save the list we've aggregated so far and reset
                    exhibitions.add(new Exhibition(prevEx.getId(), prevEx.getName(),
                            prevEx.getDescription(),
                            new ArrayList<>(artifacts)));
                    artifacts.clear();
                }

                long arId = result.getLong(4);
                String arName = result.getString(5);
                String arDesc = result.getString(6);
                // 7th column is exhibitionId
                double arLength = result.getDouble(8);
                double arWidth = result.getDouble(9);
                double arHeight = result.getDouble(10);
                double arWeight = result.getDouble(11);

                // if the last artifact field was not null, we assume all the other artifact fields
                // were not null, so add that artifact to the current aggregated list
                if (!result.wasNull()) {
                    artifacts.add(new Artifact(arId, arName, arDesc, new Dimensions(arLength,
                            arWidth, arHeight), arWeight));
                }
                prevEx = currEx;
            }
            // check for the last exhibition, finishing the sliding window
            if (prevEx != null && (exhibitions.isEmpty() ||
                    !exhibitions.get(exhibitions.size() - 1).getId().equals(prevEx.getId()))) {
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

    @Override
    public void delete(Exhibition exhibition) {
        try (var ps = db.prepareQuery("DELETE FROM exhibition WHERE id=?", exhibition.getId())) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an exhibition, and also its contained artifacts
     * @param exhibition exhibition to update
     */
    private void updateExhibition(Exhibition exhibition) {
        // hack: delete then reinsert...
        delete(exhibition);
        try (
                var ps1 = db.prepareQuery(
                        "INSERT INTO exhibition (id, name, description) VALUES (?, ?, ?)",
                        exhibition.getId(), exhibition.getName(), exhibition.getDescription())
        ) {
            ps1.executeUpdate();
            // would be better to do this in a single SQL query
            for (var artifact : exhibition.getArtifacts()) {
                try (
                        var ps2 = db.prepareQuery(
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

    /**
     * insert an exhibition that doesn't have an ID into the database, the exhibition's ID will be
     * set by this function
     * @param exhibition exhibition to insert
     */
    private void insertExhibition(Exhibition exhibition) {
        try (
                var ps = db.prepareQuery(
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
}
