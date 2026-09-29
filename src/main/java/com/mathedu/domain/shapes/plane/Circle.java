package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Circulo de radio {@code r}. */
public class Circle extends PlaneShape {

    private final double radius;

    public Circle(double radius) {
        this.radius = check(radius, "radio");
    }

    @Override
    public String name() {
        return "Circulo";
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    public double radius() {
        return radius;
    }
}
