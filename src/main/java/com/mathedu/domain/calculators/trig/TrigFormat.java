package com.mathedu.domain.calculators.trig;

import com.mathedu.shared.units.AngleUnit;

/** Formato compartido de angulos (grados + unidad seleccionada). */
final class TrigFormat {

    private TrigFormat() {
    }

    static String angle(double degrees, AngleUnit au) {
        if (au == AngleUnit.RAD) {
            return degrees + "° = " + Math.toRadians(degrees) + " rad";
        }
        return degrees + "°";
    }
}
