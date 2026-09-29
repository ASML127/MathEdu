package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Triangulo isosceles: base {@code b} y dos lados iguales {@code l}. */
public class IsoscelesTriangle extends PlaneShape {

    private final double base;
    private final double equalSide;

    public IsoscelesTriangle(double base, double equalSide) {
        check(base, "base");
        check(equalSide, "lado igual");
        if (2 * equalSide <= base) {
            throw new IllegalArgumentException(
                    "La base no puede superar la suma de los dos lados iguales.");
        }
        this.base = base;
        this.equalSide = equalSide;
    }

    @Override
    public String name() {
        return "Triangulo isosceles";
    }

    /** Altura sobre la base: h = sqrt(l^2 - (b/2)^2). */
    public double height() {
        return Math.sqrt(equalSide * equalSide - (base / 2) * (base / 2));
    }

    @Override
    public double area() {
        return base * height() / 2.0;
    }

    @Override
    public double perimeter() {
        return base + 2 * equalSide;
    }

    /** Variante con base y altura directas: A = b*h/2. */
    public static double areaBaseHeight(double base, double height) {
        check(base, "base");
        check(height, "altura");
        return base * height / 2.0;
    }
}
