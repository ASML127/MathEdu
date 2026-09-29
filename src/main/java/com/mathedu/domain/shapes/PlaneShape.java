package com.mathedu.domain.shapes;

/**
 * Figura plana: expone area y perimetro (polimorfismo).
 * Las dimensiones se validan en los {@code Calculator}; aqui solo hay
 * defensas con {@link IllegalArgumentException} ante uso directo.
 */
public abstract class PlaneShape extends Shape {

    /** Area de la figura. */
    public abstract double area();

    /** Perimetro (longitud del contorno). */
    public abstract double perimeter();

    protected static double check(double value, String what) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
            throw new IllegalArgumentException(what + " debe ser > 0 (recibido: " + value + ")");
        }
        return value;
    }
}
