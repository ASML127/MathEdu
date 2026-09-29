package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Cuadrado de lado {@code s}. */
public class Square extends PlaneShape {

    private final double side;

    public Square(double side) {
        this.side = check(side, "lado");
    }

    @Override
    public String name() {
        return "Cuadrado";
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }

    public double side() {
        return side;
    }
}
