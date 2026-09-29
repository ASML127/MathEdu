package com.mathedu.domain.shapes;

/**
 * Raiz de la jerarquia geometrica.
 * Herencia: {@link PlaneShape} y (Fase 3) {@code SolidShape} extienden esta clase.
 */
public abstract class Shape {

    /** Nombre didactico de la figura (p. ej. "Cuadrado"). */
    public abstract String name();
}
