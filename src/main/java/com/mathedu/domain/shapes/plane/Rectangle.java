package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Rectangulo de base {@code w} y altura {@code h}. */
public class Rectangle extends PlaneShape {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = check(width, "base");
        this.height = check(height, "altura");
    }

    @Override
    public String name() {
        return "Rectangulo";
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}
