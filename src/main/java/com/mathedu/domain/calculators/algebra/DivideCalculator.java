package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Division: c = a / b (b != 0). */
public class DivideCalculator implements Calculator<DivideCalculator.Input> {

    public static final String FORMULA_ID = "arith.divide";

    public record Input(double a, double b) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "División",
            "Aritmética y álgebra",
            "c = a / b",
            List.of(
                    new InputDef("a", "Dividendo (a)", "Ej. 22", 0, 0, true),
                    new InputDef("b", "Divisor (b)", "Ej. 7", 0, 0, true)),
            "Cociente",
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
        double a = Guards.requireFinite(in.a(), "a", "Dividendo");
        double b = Guards.requireNonZero(in.b(), "b", "Divisor");
        double c = Guards.requireFiniteResult(a / b);
        List<String> steps = List.of(
                "Formula: c = a / b",
                "Sustitucion: c = " + a + " / " + b,
                "Resultado: c = " + c);
        return new CalculationResult(c, "Cociente", "c = a / b", steps, "", true, null);
    }
}
