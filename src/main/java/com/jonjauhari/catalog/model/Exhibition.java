package com.jonjauhari.catalog.model;

import java.util.List;

public class Exhibition {
    private Long id;
    private String name;
    private String description;
    private List<Artifact> artifacts;

    public Exhibition(String name, String description, List<Artifact> artifacts, Long id) {
        this.name = name;
        this.description = description;
        this.artifacts = artifacts;
        this.id = id;
    }

    public Exhibition(String name, String description, List<Artifact> artifacts) {
        this.name = name;
        this.description = description;
        this.artifacts = artifacts;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
