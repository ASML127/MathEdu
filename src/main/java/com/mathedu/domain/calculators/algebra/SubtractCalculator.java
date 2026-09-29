package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Resta: d = a - b. */
public class SubtractCalculator implements Calculator<SubtractCalculator.Input> {

    public static final String FORMULA_ID = "arith.subtract";

    public record Input(double a, double b) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Resta",
            "Aritmética y álgebra",
            "d = a - b",
            List.of(
                    new InputDef("a", "Minuendo (a)", "Ej. 12.5", 0, 0, true),
                    new InputDef("b", "Sustraendo (b)", "Ej. 7.5", 0, 0, true)),
            "Resta",
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
        double a = Guards.requireFinite(in.a(), "a", "Minuendo");
        double b = Guards.requireFinite(in.b(), "b", "Sustraendo");
        double d = Guards.requireFiniteResult(a - b);
        List<String> steps = List.of(
                "Formula: d = a - b",
                "Sustitucion: d = " + a + " - " + b,
                "Resultado: d = " + d);
        return new CalculationResult(d, "Resta", "d = a - b", steps, "", true, null);
    }
}
