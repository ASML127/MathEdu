package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Suma: s = a + b. */
public class AddCalculator implements Calculator<AddCalculator.Input> {

    public static final String FORMULA_ID = "arith.add";

    public record Input(double a, double b) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Suma",
            "Aritmética y álgebra",
            "s = a + b",
            List.of(
                    new InputDef("a", "Sumando a", "Ej. 12.5", 0, 0, true),
                    new InputDef("b", "Sumando b", "Ej. 7.5", 0, 0, true)),
            "Suma",
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
        double a = Guards.requireFinite(in.a(), "a", "Sumando a");
        double b = Guards.requireFinite(in.b(), "b", "Sumando b");
        double s = Guards.requireFiniteResult(a + b);
        List<String> steps = List.of(
                "Formula: s = a + b",
                "Sustitucion: s = " + a + " + " + b,
                "Resultado: s = " + s);
        return new CalculationResult(s, "Suma", "s = a + b", steps, "", true, null);
    }
}
