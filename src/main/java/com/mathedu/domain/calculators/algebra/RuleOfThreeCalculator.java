package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Regla de tres directa: si a -> b, entonces c -> x = b*c/a. */
public class RuleOfThreeCalculator implements Calculator<RuleOfThreeCalculator.Input> {

    public static final String FORMULA_ID = "arith.rule-of-three";

    public record Input(double a, double b, double c) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Regla de tres directa",
            "Aritmética y álgebra",
            "x = b * c / a",
            List.of(
                    new InputDef("a", "A (referencia)", "Ej. 4", 0, 0, true),
                    new InputDef("b", "B (equivale a A)", "Ej. 100", 0, 0, true),
                    new InputDef("c", "C (dato)", "Ej. 6", 0, 0, true)),
            "Valor x",
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
        double a = Guards.requireNonZero(in.a(), "a", "Referencia A");
        double b = Guards.requireFinite(in.b(), "b", "Valor B");
        double c = Guards.requireFinite(in.c(), "c", "Dato C");
        double x = Guards.requireFiniteResult(b * c / a);
        List<String> steps = List.of(
                "Proporcion: " + a + " -> " + b + " ; " + c + " -> x",
                "Formula: x = b * c / a",
                "Sustitucion: x = " + b + " * " + c + " / " + a,
                "Resultado: x = " + x);
        return new CalculationResult(x, "Valor x", "x = b * c / a", steps, "", true, null);
    }
}
