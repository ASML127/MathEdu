package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Triangulo equilatero de lado {@code s}. */
public class EquilateralTriangle extends PlaneShape {

    private final double side;

    public EquilateralTriangle(double side) {
        this.side = check(side, "lado");
    }

    @Override
    public String name() {
        return "Triangulo equilatero";
    }

    /** Altura: h = s*sqrt(3)/2. */
    public double height() {
        return side * Math.sqrt(3) / 2.0;
    }

    @Override
    public double area() {
        return Math.sqrt(3) / 4.0 * side * side;
    }

    @Override
    public double perimeter() {
        return 3 * side;
    }
}
