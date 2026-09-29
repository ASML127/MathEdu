package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/**
 * Cometa / deltoide: diagonales D, d y dos pares de lados iguales a, b.
 * Area por diagonales; perimetro 2*(a+b).
 */
public class Kite extends PlaneShape {

    private final double diagonalMajor;
    private final double diagonalMinor;
    private final double sideA;
    private final double sideB;

    public Kite(double diagonalMajor, double diagonalMinor, double sideA, double sideB) {
        this.diagonalMajor = check(diagonalMajor, "diagonal mayor");
        this.diagonalMinor = check(diagonalMinor, "diagonal menor");
        this.sideA = check(sideA, "lado a");
        this.sideB = check(sideB, "lado b");
    }

    @Override
    public String name() {
        return "Cometa";
    }

    @Override
    public double area() {
        return area(diagonalMajor, diagonalMinor);
    }

    @Override
    public double perimeter() {
        return 2 * (sideA + sideB);
    }

    /** Area por diagonales: A = D*d/2. */
    public static double area(double diagonalMajor, double diagonalMinor) {
        check(diagonalMajor, "diagonal mayor");
        check(diagonalMinor, "diagonal menor");
        return diagonalMajor * diagonalMinor / 2.0;
    }
}
