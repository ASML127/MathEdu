package com.mathedu.shared.validation;

import java.util.Collections;
import java.util.Map;

/** Resultado de validar un formulario: ok o errores por campo. */
public record ValidationResult(boolean ok, Map<String, String> fieldErrors) {

    public ValidationResult {
        fieldErrors = fieldErrors == null ? Map.of() : Collections.unmodifiableMap(fieldErrors);
    }

    public static ValidationResult success() {
        return new ValidationResult(true, Map.of());
    }

    public static ValidationResult fail(Map<String, String> errors) {
        return new ValidationResult(false, errors);
    }
}
