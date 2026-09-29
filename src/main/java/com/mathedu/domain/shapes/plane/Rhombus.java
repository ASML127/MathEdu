package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/**
 * Rombo: diagonales D (mayor) y d (menor), lado l.
 * Area por diagonales; perimetro 4*l.
 */
public class Rhombus extends PlaneShape {

    private final double diagonalMajor;
    private final double diagonalMinor;
    private final double side;

    public Rhombus(double diagonalMajor, double diagonalMinor, double side) {
        this.diagonalMajor = check(diagonalMajor, "diagonal mayor");
        this.diagonalMinor = check(diagonalMinor, "diagonal menor");
        this.side = check(side, "lado");
    }

    /** Variante solo con diagonales (perimetro no disponible). */
    public Rhombus(double diagonalMajor, double diagonalMinor) {
        this(diagonalMajor, diagonalMinor, sideFromDiagonals(diagonalMajor, diagonalMinor));
    }

    private static double sideFromDiagonals(double d1, double d2) {
        return Math.sqrt((d1 / 2) * (d1 / 2) + (d2 / 2) * (d2 / 2));
    }

    @Override
    public String name() {
        return "Rombo";
    }

    @Override
    public double area() {
        return area(diagonalMajor, diagonalMinor);
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }

    /** Area por diagonales: A = D*d/2. */
    public static double area(double diagonalMajor, double diagonalMinor) {
        check(diagonalMajor, "diagonal mayor");
        check(diagonalMinor, "diagonal menor");
        return diagonalMajor * diagonalMinor / 2.0;
    }
}
