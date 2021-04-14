package com.jonjauhari.catalog.model;

import java.util.ArrayList;
import java.util.List;

public class Exhibition {
    private Long id;
    private String name;
    private String description;
    private List<Artifact> artifacts = new ArrayList<>();

    public Exhibition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Exhibition(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

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
