package com.mathedu.domain.calculators.geometry;

import com.mathedu.shared.units.LengthUnit;

/** Input validado para el area del circulo. El record garantiza inmutabilidad. */
public record CircleAreaInput(double radius, LengthUnit unit) {

    public CircleAreaInput {
        if (unit == null) {
            throw new IllegalArgumentException("unit no puede ser null");
        }
    }
}
