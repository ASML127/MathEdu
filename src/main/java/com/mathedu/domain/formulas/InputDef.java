package com.mathedu.domain.formulas;

import java.util.Objects;

/** Describe un dato de entrada para generar el formulario dinamico. */
public record InputDef(
        String key,
        String label,
        String placeholder,
        double min,
        double max,
        boolean required) {

    public InputDef {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(label, "label");
    }

    public static InputDef positive(String key, String label, String placeholder) {
        return new InputDef(key, label, placeholder, 0.0, Double.MAX_VALUE, true);
    }

    /** Campo de texto libre (expresion, operador, coeficientes). Sin rango numerico. */
    public static InputDef text(String key, String label, String placeholder) {
        return new InputDef(key, label, placeholder, 0.0, 0.0, true);
    }

    /** Numero opcional con valor por defecto si se deja en blanco. */
    public static InputDef optional(String key, String label, String placeholder) {
        return new InputDef(key, label, placeholder, 0.0, 0.0, false);
    }
}
