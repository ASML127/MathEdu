package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Valor absoluto: r = |x|. */
public class AbsCalculator implements Calculator<AbsCalculator.Input> {

    public static final String FORMULA_ID = "arith.abs";

    public record Input(double x) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Valor absoluto",
            "Aritmética y álgebra",
            "r = |x|",
            List.of(new InputDef("x", "Número (x)", "Ej. -7.5", 0, 0, true)),
            "Valor absoluto",
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
        double x = Guards.requireFinite(in.x(), "x", "Numero");
        double r = Math.abs(x);
        List<String> steps = List.of(
                "Definicion: |x| = x si x >= 0, -x si x < 0",
                "Sustitucion: |" + x + "|",
                "Resultado: r = " + r);
        return new CalculationResult(r, "Valor absoluto", "r = |x|", steps, "", true, null);
    }
}
