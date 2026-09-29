package com.mathedu.domain.formulas;

import java.util.List;
import java.util.Objects;

/**
 * Metadatos de una formula: lo que la UI necesita sin conocer la matematica.
 * Fase 1 generara el formulario completo a partir de esto.
 */
public record FormulaDef(
        String id,
        String displayName,
        String category,
        String expressionTemplate,
        List<InputDef> inputs,
        String resultLabel,
        String unitHint) {

    public FormulaDef {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        inputs = inputs == null ? List.of() : List.copyOf(inputs);
    }
}
