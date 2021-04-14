package com.jonjauhari.catalog.model;

import java.util.Objects;

public class Artifact {
    private Long id;
    private String name;
    private String description;
    private Exhibition location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(id, artifact.id) &&
                name.equals(artifact.name) &&
                description.equals(artifact.description) &&
                Objects.equals(location, artifact.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location);
    }

    public Artifact(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Artifact(String name, String description) {
        this(null, name, description);
    }

    public Exhibition getLocation() {
        return location;
    }

    public void setLocation(Exhibition location) {
        this.location = location;
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
        return "#" + id + " " + name + " | " + description;
    }
}
