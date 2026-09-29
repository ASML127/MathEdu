package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.algebra.Polynomial;
import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Evaluacion de un polinomio en x por el metodo de Horner. */
public class PolyEvaluateCalculator implements Calculator<PolyEvaluateCalculator.Input> {

    public static final String FORMULA_ID = "arith.poly-eval";

    public record Input(String coefficients, double x) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Evaluar polinomio",
            "Aritmética y álgebra",
            "P(x) por Horner",
            List.of(
                    InputDef.text("coeffs", "Coeficientes (grado mayor primero)", "Ej. 2, -3, 1"),
                    new InputDef("x", "Valor de x", "Ej. 2", 0, 0, true)),
            "P(x)",
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
        Polynomial p = Polynomial.parse(in.coefficients(), "coeffs");
        double x = Guards.requireFinite(in.x(), "x", "Valor de x");
        double value = Guards.requireFiniteResult(p.evaluate(x));
        List<String> steps = List.of(
                "Polinomio: P(x) = " + p.toExpressionString(),
                "Horner: P(" + x + ") = " + p.hornerForm(),
                "Resultado: P(" + x + ") = " + value);
        return new CalculationResult(value, "P(x)", "Horner: evaluacion anidada",
                steps, "", true, null);
    }
}
