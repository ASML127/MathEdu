package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Triangulo rectangulo definido por sus catetos {@code a} y {@code b}. */
public class RightTriangle extends PlaneShape {

    private final double legA;
    private final double legB;

    public RightTriangle(double legA, double legB) {
        this.legA = check(legA, "cateto a");
        this.legB = check(legB, "cateto b");
    }

    @Override
    public String name() {
        return "Triangulo rectangulo";
    }

    /** Hipotenusa por Pitagoras: c = sqrt(a^2 + b^2). */
    public double hypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public double area() {
        return legA * legB / 2.0;
    }

    @Override
    public double perimeter() {
        return legA + legB + hypotenuse();
    }
}
