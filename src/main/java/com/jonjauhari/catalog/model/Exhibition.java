package com.jonjauhari.catalog.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of artifacts in a museum. An artifact can only be in a part of one exhibition
 * at a time, but an exhibition can also be empty.
 */
public class Exhibition {
    private Long id;
    private String name;
    private String description;
    private List<Artifact> artifacts = new ArrayList<>();

    /**
     * Create an exhibition without an ID. This implies it is not persisted in the database
     * @param name the name of the exhibition
     * @param description a longer description of the exhibition
     */
    public Exhibition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Create an exhibition with an ID. This implies it is already persisted in the database
     * @param id the ID of the exhibition, as in the database
     * @param name the name of the exhibition
     * @param description a longer description of the exhibition
     */
    public Exhibition(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Create an exhibition with an ID. This implies it is already persisted in the database
     * @param id the ID of the exhibition, as in the database
     * @param name the name of the exhibition
     * @param description a longer description of the exhibition
     * @param artifacts the artifacts in this exhibition
     */
    public Exhibition(Long id, String name, String description, List<Artifact> artifacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setLocation(this);
        artifacts.add(artifact);
    }

    public void deleteArtifact(Artifact artifact) {
        artifact.setLocation(null);
        artifacts.remove(artifact);
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "#" + id + " " + name + " (" + artifacts.size() + ")" +  " | " + description;
    }
}
