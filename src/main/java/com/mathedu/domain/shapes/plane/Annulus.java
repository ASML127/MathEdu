package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Corona circular: radio exterior R y radio interior r (R > r). */
public class Annulus extends PlaneShape {

    private final double outerRadius;
    private final double innerRadius;

    public Annulus(double outerRadius, double innerRadius) {
        check(outerRadius, "radio exterior");
        check(innerRadius, "radio interior");
        if (outerRadius <= innerRadius) {
            throw new IllegalArgumentException(
                    "El radio exterior debe ser mayor que el interior.");
        }
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
    }

    @Override
    public String name() {
        return "Corona circular";
    }

    @Override
    public double area() {
        return Math.PI * (outerRadius * outerRadius - innerRadius * innerRadius);
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * (outerRadius + innerRadius);
    }
}
