package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Que porcentaje es 'a' de 'b': p = 100 * a / b. */
public class PercentOfCalculator implements Calculator<PercentOfCalculator.Input> {

    public static final String FORMULA_ID = "arith.percent-of";

    public record Input(double part, double total) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Qué % es una cantidad de otra",
            "Aritmética y álgebra",
            "p = 100 * a / b",
            List.of(
                    new InputDef("part", "Parte (a)", "Ej. 30", 0, 0, true),
                    new InputDef("total", "Total (b)", "Ej. 200", 0, 0, true)),
            "Porcentaje",
            "%");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double a = Guards.requireFinite(in.part(), "part", "Parte");
        double b = Guards.requireNonZero(in.total(), "total", "Total");
        double p = Guards.requireFiniteResult(100.0 * a / b);
        List<String> steps = List.of(
                "Formula: p = 100 * a / b",
                "Sustitucion: p = 100 * " + a + " / " + b,
                "Resultado: " + a + " es el " + p + "% de " + b);
        return new CalculationResult(p, "Porcentaje", "p = 100 * a / b", steps, "%", true, null);
    }
}
