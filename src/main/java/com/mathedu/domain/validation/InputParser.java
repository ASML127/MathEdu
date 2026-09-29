package com.mathedu.domain.validation;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

/**
 * Parsea texto de la UI a numeros. Acepta coma decimal ("3,5").
 * Toda entrada malformada produce {@link CalcException} con campo.
 */
public final class InputParser {

    private InputParser() {
    }

    public static double parseDouble(String raw, String field, String label) throws CalcException {
        String text = raw == null ? "" : raw.trim().replace(',', '.');
        if (text.isEmpty()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    "Introduce " + label + ".");
        }
        final double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    "«" + raw.trim() + "» no es un numero valido para " + label + ". Ej.: 3.5");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    label + " debe ser un numero finito.");
        }
        return value;
    }

    public static int parseInt(String raw, String field, String label) throws CalcException {
        double value = parseDouble(raw, field, label);
        if (value != Math.rint(value)) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    label + " debe ser un numero entero. Recibido: " + raw.trim());
        }
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new CalcException(ErrorCode.OVERFLOW, field,
                    label + " fuera de rango entero.");
        }
        return (int) value;
    }

    /** Numero opcional: en blanco devuelve el valor por defecto (sin error). */
    public static double parseDoubleOrDefault(String raw, String field, String label,
                                              double defaultValue) throws CalcException {
        String text = raw == null ? "" : raw.trim();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return parseDouble(raw, field, label);
    }

    /** Texto libre no vacio (operadores, expresiones, coeficientes). */
    public static String parseText(String raw, String field, String label) throws CalcException {
        String text = raw == null ? "" : raw.trim();
        if (text.isEmpty()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    "Introduce " + label + ".");
        }
        return text;
    }
}
