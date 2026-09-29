package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Paralelogramo: base, lado oblicuo y altura sobre la base. */
public class Parallelogram extends PlaneShape {

    private final double base;
    private final double side;
    private final double height;

    public Parallelogram(double base, double side, double height) {
        this.base = check(base, "base");
        this.side = check(side, "lado");
        this.height = check(height, "altura");
    }

    @Override
    public String name() {
        return "Paralelogramo";
    }

    @Override
    public double area() {
        return area(base, height);
    }

    @Override
    public double perimeter() {
        return 2 * (base + side);
    }

    /** Area con base y altura: A = b*h. */
    public static double area(double base, double height) {
        check(base, "base");
        check(height, "altura");
        return base * height;
    }
}
