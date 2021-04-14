package com.jonjauhari.catalog.repository;

import com.jonjauhari.catalog.model.Artifact;
import com.jonjauhari.catalog.model.Dimensions;
import com.jonjauhari.catalog.model.Exhibition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionRepository implements Repository<Exhibition, Long> {

    private Database db;

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
                     db.prepare("SELECT * FROM exhibition AS ex LEFT JOIN artifact AS ar ON ex.id" +
                             " " +
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
                double arLength = result.getDouble(7);
                double arWidth = result.getDouble(8);
                double arHeight = result.getDouble(9);
                double arWeight = result.getDouble(10);

                if (!result.wasNull()) {
                    artifacts.add(new Artifact(arId, arName, arDesc, new Dimensions(arLength,
                            arWidth, arHeight), arWeight));
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

    @Override
    public void delete(Exhibition exhibition) {
        try (var ps = db.prepare("DELETE FROM exhibition WHERE id=?", exhibition.getId())) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateExhibition(Exhibition exhibition) {
        delete(exhibition);
        try (
                var ps1 = db.prepare(
                        "INSERT INTO exhibition (id, name, description) VALUES (?, ?, ?)",
                        exhibition.getId(), exhibition.getName(), exhibition.getDescription())
        ) {
            ps1.executeUpdate();
            for (var artifact : exhibition.getArtifacts()) {
                try (
                        var ps2 = db.prepare(
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

    private void insertExhibition(Exhibition exhibition) {
        try (
                var ps = db.prepare(
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
