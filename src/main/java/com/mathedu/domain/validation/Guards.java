package com.mathedu.domain.validation;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

/**
 * Guardas de dominio: validaciones reutilizables con mensajes didacticos.
 * Evitan codigo duplicado en las 30+ calculadoras (DRY).
 */
public final class Guards {

    /** Magnitud maxima aceptada antes de declarar overflow. */
    public static final double MAX_MAGNITUDE = 1e9;

    private Guards() {
    }

    public static double requireFinite(double value, String field, String label) throws CalcException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    label + " debe ser un numero finito.");
        }
        return value;
    }

    public static double requirePositive(double value, String field, String label) throws CalcException {
        requireFinite(value, field, label);
        if (value <= 0) {
            throw new CalcException(ErrorCode.INVALID_RANGE, field,
                    label + " debe ser mayor que cero. Recibido: " + value);
        }
        return requireReasonable(value, field, label);
    }

    public static double requireReasonable(double value, String field, String label) throws CalcException {
        if (Math.abs(value) > MAX_MAGNITUDE) {
            throw new CalcException(ErrorCode.OVERFLOW, field,
                    label + " demasiado grande (max 1e9). Podria desbordar el calculo.");
        }
        return value;
    }

    /** Distinto de cero (divisores). */
    public static double requireNonZero(double value, String field, String label) throws CalcException {
        requireFinite(value, field, label);
        if (value == 0) {
            throw new CalcException(ErrorCode.DIV_BY_ZERO, field,
                    label + " no puede ser cero (division entre cero).");
        }
        return requireReasonable(value, field, label);
    }

    public static double requireAngleDeg(double angleDeg, String field) throws CalcException {
        requireFinite(angleDeg, field, "Angulo");
        if (angleDeg <= 0 || angleDeg > 360) {
            throw new CalcException(ErrorCode.INVALID_RANGE, field,
                    "El angulo debe estar en (0, 360] grados. Recibido: " + angleDeg);
        }
        return angleDeg;
    }

    public static void requireTriangleInequality(double a, double b, double c) throws CalcException {
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "a",
                    "Esos lados no forman un triangulo: ningun lado puede superar "
                            + "la suma de los otros dos (desigualdad triangular).");
        }
    }

    public static void requireOuterGreaterThanInner(double outer, double inner) throws CalcException {
        if (outer <= inner) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "R",
                    "El radio exterior R debe ser mayor que el interior r.");
        }
    }

    public static int requireSides(int n) throws CalcException {
        if (n < 3) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "n",
                    "Un poligono necesita al menos 3 lados. Recibido: " + n);
        }
        if (n > 1_000_000) {
            throw new CalcException(ErrorCode.OVERFLOW, "n",
                    "Demasiados lados (max 1 000 000).");
        }
        return n;
    }

    public static double requireFiniteResult(double value) throws CalcException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalcException(ErrorCode.OVERFLOW,
                    "El resultado no es un numero finito (desbordamiento). Revisa los datos.");
        }
        return value;
    }
}
