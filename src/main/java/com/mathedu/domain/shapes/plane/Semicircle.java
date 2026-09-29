package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Semicirculo de radio {@code r} (mitad del circulo + diametro). */
public class Semicircle extends PlaneShape {

    private final double radius;

    public Semicircle(double radius) {
        this.radius = check(radius, "radio");
    }

    @Override
    public String name() {
        return "Semicirculo";
    }

    @Override
    public double area() {
        return Math.PI * radius * radius / 2.0;
    }

    @Override
    public double perimeter() {
        return Math.PI * radius + 2 * radius;
    }
}
