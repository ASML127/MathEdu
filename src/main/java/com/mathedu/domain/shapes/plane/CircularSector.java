package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Sector circular: radio {@code r} y angulo en grados (0, 360]. */
public class CircularSector extends PlaneShape {

    private final double radius;
    private final double angleDeg;

    public CircularSector(double radius, double angleDeg) {
        this.radius = check(radius, "radio");
        if (Double.isNaN(angleDeg) || angleDeg <= 0 || angleDeg > 360) {
            throw new IllegalArgumentException(
                    "El angulo debe estar en (0, 360] grados (recibido: " + angleDeg + ")");
        }
        this.angleDeg = angleDeg;
    }

    @Override
    public String name() {
        return "Sector circular";
    }

    /** Longitud del arco: L = 2*pi*r*theta/360. */
    public double arcLength() {
        return 2 * Math.PI * radius * angleDeg / 360.0;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius * angleDeg / 360.0;
    }

    @Override
    public double perimeter() {
        return arcLength() + 2 * radius;
    }
}
