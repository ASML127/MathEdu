package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/** Trapecio: bases B (mayor) y b (menor), lados no paralelos c, d. */
public class Trapezoid extends PlaneShape {

    private final double bigBase;
    private final double smallBase;
    private final double sideC;
    private final double sideD;

    public Trapezoid(double bigBase, double smallBase, double sideC, double sideD) {
        this.bigBase = check(bigBase, "base mayor");
        this.smallBase = check(smallBase, "base menor");
        this.sideC = check(sideC, "lado c");
        this.sideD = check(sideD, "lado d");
    }

    @Override
    public String name() {
        return "Trapecio";
    }

    @Override
    public double area() {
        throw new UnsupportedOperationException(
                "El area del trapecio necesita la altura: usa area(bigBase, smallBase, height).");
    }

    @Override
    public double perimeter() {
        return bigBase + smallBase + sideC + sideD;
    }

    /** Area: A = (B + b)*h/2. */
    public static double area(double bigBase, double smallBase, double height) {
        check(bigBase, "base mayor");
        check(smallBase, "base menor");
        check(height, "altura");
        return (bigBase + smallBase) * height / 2.0;
    }
}
