package com.mathedu.shared.result;

import java.util.List;
import java.util.Objects;

/**
 * Resultado inmutable de un calculo.
 *
 * <p>Contrato transversal: todo {@code Calculator} devuelve esto.
 * La UI solo consume este DTO, nunca las entidades internas.
 *
 * @param value       valor numerico principal
 * @param label       etiqueta corta (p. ej. "Area")
 * @param formula     formula utilizada (p. ej. "A = pi * r^2")
 * @param steps       explicacion paso a paso con sustitucion
 * @param unitSymbol  simbolo de unidad para mostrar ("cm^2", "m^3", "rad", "")
 * @param exact       true si es simbolico/exacto, false si es aproximacion numerica
 * @param warning     aviso opcional (puede ser null)
 */
public record CalculationResult(
        double value,
        String label,
        String formula,
        List<String> steps,
        String unitSymbol,
        boolean exact,
        String warning) {

    public CalculationResult {
        Objects.requireNonNull(label, "label");
        Objects.requireNonNull(formula, "formula");
        steps = steps == null ? List.of() : List.copyOf(steps);
        unitSymbol = unitSymbol == null ? "" : unitSymbol;
    }

    /** Texto listo para copiar/portapapeles. */
    public String toDisplayString() {
        String base = "%s = %s %s%nFormula: %s".formatted(
                label, format(value), unitSymbol.isBlank() ? "" : unitSymbol, formula);
        if (warning != null && !warning.isBlank()) {
            base += "%nAviso: %s".formatted(warning);
        }
        return base;
    }

    private static String format(double v) {
        double abs = Math.abs(v);
        if (abs != 0 && (abs >= 1e9 || abs < 1e-6)) {
            return String.format("%.6e", v);
        }
        return String.format("%.6f", v).replaceAll("0+$", "").replaceAll("\\.$", ".0");
    }
}
