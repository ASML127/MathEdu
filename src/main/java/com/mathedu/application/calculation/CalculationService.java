package com.mathedu.application.calculation;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Orquesta calculos: busca el Calculator, lo ejecuta y aplica
 * los guardas transversales (NaN/Infinity). La UI lo llama
 * dentro de un {@code javafx.concurrent.Task} (no bloquea).
 */
public class CalculationService {

    private static final Logger log = LoggerFactory.getLogger(CalculationService.class);

    private final CalculatorRegistry registry;

    public CalculationService(CalculatorRegistry registry) {
        this.registry = registry;
    }

    public <T> CalculationResult compute(String formulaId, T input) throws CalcException {
        Calculator<T> calc = registry.get(formulaId);
        log.debug("Calculando {}", formulaId);
        CalculationResult result = calc.calculate(input);
        if (Double.isNaN(result.value()) || Double.isInfinite(result.value())) {
            throw new CalcException(ErrorCode.OVERFLOW,
                    "El resultado no es un numero finito (NaN/Infinito). Revisa los datos.");
        }
        return result;
    }

    /** Metadatos de todas las formulas registradas (para el selector de la UI). */
    public List<FormulaDef> formulas() {
        return registry.all().stream().map(Calculator::formula).toList();
    }
}
