package com.mathedu.application.calculation;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Unico punto de alta de formulas (Open/Closed):
 * anadir una formula = registrar aqui, sin tocar la UI.
 */
public class CalculatorRegistry {

    private final Map<String, Calculator<?>> byId = new LinkedHashMap<>();

    public <T> void register(Calculator<T> calculator) {
        byId.put(calculator.formula().id(), calculator);
    }

    @SuppressWarnings("unchecked")
    public <T> Calculator<T> get(String formulaId) throws CalcException {
        Calculator<?> calc = byId.get(formulaId);
        if (calc == null) {
            throw new CalcException(ErrorCode.UNKNOWN_FORMULA,
                    "formulaId", "Formula desconocida: " + formulaId);
        }
        return (Calculator<T>) calc;
    }

    public Collection<Calculator<?>> all() {
        return byId.values();
    }

    public boolean contains(String formulaId) {
        return byId.containsKey(formulaId);
    }
}
