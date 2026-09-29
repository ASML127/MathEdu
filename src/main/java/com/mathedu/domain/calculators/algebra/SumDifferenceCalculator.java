package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Producto notable: (a + b)(a - b) = a^2 - b^2 (suma por diferencia). */
public class SumDifferenceCalculator implements Calculator<SumDifferenceCalculator.Input> {

    public static final String FORMULA_ID = "arith.sum-diff";

    public record Input(double a, double b) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Producto notable (a+b)(a−b)",
            "Aritmética y álgebra",
            "(a + b)(a - b) = a^2 - b^2",
            List.of(
                    new InputDef("a", "Término a", "Ej. 7", 0, 0, true),
                    new InputDef("b", "Término b", "Ej. 2", 0, 0, true)),
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
        double a = Guards.requireFinite(in.a(), "a", "Termino a");
        double b = Guards.requireFinite(in.b(), "b", "Termino b");
        double r = Guards.requireFiniteResult((a + b) * (a - b));
        List<String> steps = List.of(
                "Identidad: (a + b)(a - b) = a^2 - b^2",
                "Desarrollo: (" + a + ")^2 - (" + b + ")^2 = " + (a * a) + " - " + (b * b),
                "Resultado: " + r);
        return new CalculationResult(r, "Resultado", "(a + b)(a - b) = a^2 - b^2",
                steps, "", true, null);
    }
}
