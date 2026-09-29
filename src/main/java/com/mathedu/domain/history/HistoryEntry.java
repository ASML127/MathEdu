package com.mathedu.domain.history;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Entrada del historial: que se calculo, con que datos y que salio.
 * Inmutable y serializable a JSON (Jackson).
 */
public record HistoryEntry(
        long epochMillis,
        String category,
        String formulaId,
        String formulaName,
        Map<String, String> inputs,
        double resultValue,
        String unitSymbol,
        String formula,
        String lengthUnit,
        String angleUnit) {

    public HistoryEntry {
        inputs = inputs == null ? Map.of() : Map.copyOf(inputs);
        category = category == null ? "" : category;
        formulaName = formulaName == null ? "" : formulaName;
        unitSymbol = unitSymbol == null ? "" : unitSymbol;
        formula = formula == null ? "" : formula;
        lengthUnit = lengthUnit == null ? "" : lengthUnit;
        angleUnit = angleUnit == null ? "" : angleUnit;
    }

    /** Una linea legible para el ListView del historial. */
    public String displaySummary() {
        String in = inputs.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
        String value = String.format("%.4f", resultValue)
                .replaceAll("0+$", "").replaceAll("\\.$", ".0");
        return "%s | %s [%s] = %s %s".formatted(
                formulaName, in, lengthUnit, value, unitSymbol).trim();
    }
}
