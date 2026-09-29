package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Raiz n-esima real de x (indice entero). */
public class RootCalculator implements Calculator<RootCalculator.Input> {

    public static final String FORMULA_ID = "arith.root";

    public record Input(double radicand, int index) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Raíz n-ésima",
            "Aritmética y álgebra",
            "r = n√x",
            List.of(
                    new InputDef("x", "Radicando (x)", "Ej. 27", 0, 0, true),
                    new InputDef("n", "Índice (n)", "Ej. 3", 0, 0, true)),
            "Raíz",
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
        double x = Guards.requireFinite(in.radicand(), "x", "Radicando");
        int n = in.index();
        if (n == 0) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "n",
                    "El indice de la raiz no puede ser cero.");
        }
        if (Math.abs(n) > 1_000_000) {
            throw new CalcException(ErrorCode.OVERFLOW, "n", "Indice demasiado grande.");
        }
        double r;
        if (x < 0) {
            if (n % 2 == 0) {
                throw new CalcException(ErrorCode.DOMAIN_ERROR, "x",
                        "Raiz par de numero negativo (sin solucion real).");
            }
            r = -Math.pow(-x, 1.0 / n);
            if (n < 0) {
                r = 1.0 / r;
            }
        } else {
            r = Math.pow(x, 1.0 / n);
            if (n < 0) {
                r = 1.0 / r;
            }
        }
        r = Guards.requireFiniteResult(r);
        List<String> steps = List.of(
                "Formula: r = n√x = x^(1/n)",
                "Sustitucion: r = (" + x + ")^(1/" + n + ")",
                x < 0 ? "Radicando negativo con indice impar: resultado negativo." : "Radicando no negativo.",
                "Resultado: r = " + r);
        return new CalculationResult(r, "Raíz", "r = n√x", steps, "", true, null);
    }
}
