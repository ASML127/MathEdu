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
 * Division euclidea de polinomios: P = Q·C + R.
 * El valor principal es el grado del cociente.
 */
public class PolyDivideCalculator implements Calculator<PolyDivideCalculator.Input> {

    public static final String FORMULA_ID = "arith.poly-div";

    public record Input(String coeffA, String coeffB) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "División de polinomios",
            "Aritmética y álgebra",
            "P(x) = Q(x)·C(x) + R(x)",
            List.of(
                    InputDef.text("coeffA", "Dividendo P", "Ej. 1, -6, 11, -6"),
                    InputDef.text("coeffB", "Divisor Q", "Ej. 1, -1")),
            "Grado del cociente",
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
        Polynomial.QuotientRemainder qr = p.divide(q);
        List<String> steps = List.of(
                "Dividendo: P(x) = " + p.toExpressionString(),
                "Divisor: Q(x) = " + q.toExpressionString(),
                "Division larga termino a termino (grado mayor primero).",
                "Cociente: C(x) = " + qr.quotient().toExpressionString(),
                "Resto: R(x) = " + qr.remainder().toExpressionString(),
                "Verificacion: P = Q·C + R");
        return new CalculationResult(qr.quotient().degree(), "Grado del cociente",
                "P(x) = Q(x)·C(x) + R(x)", steps, "", true, null);
    }
}
