package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.algebra.Polynomial;
import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/**
 * Suma de polinomios. El valor principal es el grado del resultado;
 * el polinomio completo va en los pasos (el modelo de resultado es escalar).
 */
public class PolyAddCalculator implements Calculator<PolyAddCalculator.Input> {

    public static final String FORMULA_ID = "arith.poly-add";

    public record Input(String coeffA, String coeffB) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Suma de polinomios",
            "Aritmética y álgebra",
            "(P + Q)(x)",
            List.of(
                    InputDef.text("coeffA", "P: coeficientes", "Ej. 2, -3, 1"),
                    InputDef.text("coeffB", "Q: coeficientes", "Ej. 1, 5")),
            "Grado del resultado",
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
        Polynomial p = Polynomial.parse(in.coeffA(), "coeffA");
        Polynomial q = Polynomial.parse(in.coeffB(), "coeffB");
        Polynomial r = p.add(q);
        List<String> steps = List.of(
                "P(x) = " + p.toExpressionString(),
                "Q(x) = " + q.toExpressionString(),
                "Suma termino a termino (mismo grado).",
                "Resultado: R(x) = " + r.toExpressionString());
        return new CalculationResult(r.degree(), "Grado del resultado",
                "R(x) = P(x) + Q(x)", steps, "", true, null);
    }
}
