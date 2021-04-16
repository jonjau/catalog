package com.jonjauhari.catalog.model;

/**
 * Describes length, width, height of an object
 */
public class Dimensions {
    public final double length;
    public final double width;
    public final double height;

    /**
     * @param length in meters
     * @param width in meters
     * @param height in meters
     */
    public Dimensions(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the cubic volume (m^3) described by the length, width, height of this instance
     */
    public double getVolume() {
        return length * width * height;
    }
}
