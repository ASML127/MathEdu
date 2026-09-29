package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Ecuacion de primer grado: ax + b = c. */
public class LinearEquationCalculator implements Calculator<LinearEquationCalculator.Input> {

    public static final String FORMULA_ID = "arith.linear-eq";

    public record Input(double a, double b, double c) {
        public Input(double a, double b) {
            this(a, b, 0);
        }
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Ecuación de primer grado",
            "Aritmética y álgebra",
                "ax + b = c  →  x = (c - b)/a",
            List.of(
                    new InputDef("a", "Coeficiente a", "Ej. 3", 0, 0, true),
                    new InputDef("b", "Término b", "Ej. 3", 0, 0, true),
                    new InputDef("c", "Segundo miembro c", "Ej. 7", 0, 0, true)),
            "Solución x",
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
        double a = Guards.requireFinite(in.a(), "a", "Coeficiente a");
        double b = Guards.requireFinite(in.b(), "b", "Termino b");
        double c = Guards.requireFinite(in.c(), "c", "Segundo miembro c");
        if (a == 0) {
            if (b == c) {
                throw new CalcException(ErrorCode.INFINITE_SOLUTIONS,
                b + " = " + c + ": la ecuacion tiene infinitas soluciones (identidad).");
            }
            throw new CalcException(ErrorCode.NO_SOLUTION,
                b + " = " + c + ": la ecuacion no tiene solucion (contradiccion).");
        }
        double x = Guards.requireFiniteResult((c - b) / a);
        List<String> steps = List.of(
            "Ecuacion: " + a + "x + (" + b + ") = " + c,
            "Despeje: x = (c - b)/a = (" + c + " - (" + b + "))/" + a,
                "Resultado: x = " + x);
        return new CalculationResult(x, "Solución x", "x = (c - b)/a", steps, "", true, null);
    }
}
