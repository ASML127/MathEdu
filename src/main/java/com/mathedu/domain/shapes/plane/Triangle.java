package com.mathedu.domain.shapes.plane;

import com.mathedu.domain.shapes.PlaneShape;

/**
 * Triangulo general definido por sus 3 lados.
 * Area por formula de Heron; la variante base*altura vive como
 * metodo estatico (no hay lados suficientes para el perimetro).
 */
public class Triangle extends PlaneShape {

    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        check(a, "lado a");
        check(b, "lado b");
        check(c, "lado c");
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException(
                    "Desigualdad triangular: ningun lado puede superar la suma de los otros dos.");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String name() {
        return "Triangulo";
    }

    @Override
    public double area() {
        double s = perimeter() / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    /** Area con base y altura: A = b*h/2. */
    public static double areaBaseHeight(double base, double height) {
        check(base, "base");
        check(height, "altura");
        return base * height / 2.0;
    }
}
