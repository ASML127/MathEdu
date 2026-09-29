package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Poligono regular de {@code n} lados (n >= 3) de longitud {@code side}. */
public class RegularPolygon extends PlaneShape {

    private final int sides;
    private final double side;

    public RegularPolygon(int sides, double side) {
        if (sides < 3) {
            throw new IllegalArgumentException(
                    "Un poligono necesita al menos 3 lados (recibido: " + sides + ")");
        }
        this.sides = sides;
        this.side = check(side, "lado");
    }

    @Override
    public String name() {
        return "Poligono regular";
    }

    /** Apotema: ap = s / (2*tan(pi/n)). */
    public double apothem() {
        return side / (2 * Math.tan(Math.PI / sides));
    }

    @Override
    public double area() {
        return sides * side * side / (4 * Math.tan(Math.PI / sides));
    }

    @Override
    public double perimeter() {
        return sides * side;
    }
}
