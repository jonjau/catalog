package com.jonjauhari.catalog.model;

public class Dimensions {
    // units are m, kg
    public final double length;
    public final double width;
    public final double height;

    public Dimensions(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public double getVolume() {
        return length * width * height;
    }
}
