package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Exponencial: r = e^x. */
public class ExpCalculator implements Calculator<ExpCalculator.Input> {

    public static final String FORMULA_ID = "arith.exp";

    public record Input(double x) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Exponencial (e^x)",
            "Aritmética y álgebra",
            "r = e^x",
            List.of(new InputDef("x", "Exponente (x)", "Ej. 2", 0, 0, true)),
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
        double x = Guards.requireFinite(in.x(), "x", "Exponente");
        if (x > 709) {
            throw new CalcException(ErrorCode.OVERFLOW, "x",
                    "e^x desborda el rango double para x > 709.");
        }
        double r = Guards.requireFiniteResult(Math.exp(x));
        List<String> steps = List.of(
                "Formula: r = e^x  (e ≈ 2.71828)",
                "Sustitucion: r = e^(" + x + ")",
                "Resultado: r = " + r);
        return new CalculationResult(r, "Resultado", "r = e^x", steps, "", true, null);
    }
}
