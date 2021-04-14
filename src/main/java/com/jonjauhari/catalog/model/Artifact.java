package com.jonjauhari.catalog.model;

import java.util.Objects;

/**
 * A physical artifact stored in a Museum
 */
public class Artifact {
    private Long id;
    private String name;
    private String description;

    private Dimensions dimensions;
    private double weight;

    // if location is null, then artifact is in storage.
    private Exhibition location;

    /**
     * Create an artifact with an ID. This implies the artifact is already persisted in the database
     * @param id the ID of the artifact, as in the database
     * @param name the name of the artifact
     * @param description a longer description of the artifact
     * @param dimensions the 3 dimensional specifications of this artifact in meters
     * @param weight the weight of the artifact in kilograms
     */
    public Artifact(Long id, String name, String description, Dimensions dimensions,
                    double weight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    /**
     * Create an artifact without an ID. This implies the artifact is not persisted in the database
     * @param name the name of the artifact
     * @param description a longer description of the artifact
     * @param dimensions the 3 dimensional specifications of this artifact in meters
     * @param weight the weight of the artifact in kilograms
     */
    public Artifact(String name, String description, Dimensions dimensions, double weight) {
        this(null, name, description, dimensions, weight);
    }

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

    @Override
    public String toString() {
        return "#" + id + " " + name + " | " + description;
    }

    public double getVolume() {
        return dimensions.getVolume();
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
}
