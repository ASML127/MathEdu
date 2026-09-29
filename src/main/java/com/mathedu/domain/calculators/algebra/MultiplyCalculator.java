package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Multiplicacion: p = a * b. */
public class MultiplyCalculator implements Calculator<MultiplyCalculator.Input> {

    public static final String FORMULA_ID = "arith.multiply";

    public record Input(double a, double b) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Multiplicación",
            "Aritmética y álgebra",
            "p = a * b",
            List.of(
                    new InputDef("a", "Factor a", "Ej. 12.5", 0, 0, true),
                    new InputDef("b", "Factor b", "Ej. 4", 0, 0, true)),
            "Producto",
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
        double a = Guards.requireFinite(in.a(), "a", "Factor a");
        double b = Guards.requireFinite(in.b(), "b", "Factor b");
        double p = Guards.requireFiniteResult(a * b);
        List<String> steps = List.of(
                "Formula: p = a * b",
                "Sustitucion: p = " + a + " * " + b,
                "Resultado: p = " + p);
        return new CalculationResult(p, "Producto", "p = a * b", steps, "", true, null);
    }
}
