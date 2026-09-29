package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Porcentaje de una cantidad: r = p% de N = N * p / 100. */
public class PercentCalculator implements Calculator<PercentCalculator.Input> {

    public static final String FORMULA_ID = "arith.percent";

    public record Input(double percent, double base) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Porcentaje de una cantidad",
            "Aritmética y álgebra",
            "r = N * p / 100",
            List.of(
                    new InputDef("p", "Porcentaje (p)", "Ej. 15", 0, 0, true),
                    new InputDef("base", "Cantidad (N)", "Ej. 200", 0, 0, true)),
            "Resultado",
            "");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double p = Guards.requireFinite(in.percent(), "p", "Porcentaje");
        double n = Guards.requireFinite(in.base(), "base", "Cantidad");
        double r = Guards.requireFiniteResult(n * p / 100.0);
        List<String> steps = List.of(
                "Formula: r = N * p / 100",
                "Sustitucion: r = " + n + " * " + p + " / 100",
                "Resultado: r = " + r + "  (el " + p + "% de " + n + ")");
        return new CalculationResult(r, "Resultado", "r = N * p / 100", steps, "", true, null);
    }
}
