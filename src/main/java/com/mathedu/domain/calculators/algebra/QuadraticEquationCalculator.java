package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Ecuacion de segundo grado: ax^2 + bx + c = 0.
 * El valor principal es x1; x2 se detalla en los pasos.
 * Si a = 0 se resuelve como lineal (con aviso).
 */
public class QuadraticEquationCalculator implements Calculator<QuadraticEquationCalculator.Input> {

    public static final String FORMULA_ID = "arith.quadratic-eq";

    public record Input(double a, double b, double c) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Ecuación de segundo grado",
            "Aritmética y álgebra",
            "ax^2 + bx + c = 0  →  x = (-b ± √D) / 2a",
            List.of(
                    new InputDef("a", "Coeficiente a", "Ej. 1", 0, 0, true),
                    new InputDef("b", "Coeficiente b", "Ej. -5", 0, 0, true),
                    new InputDef("c", "Término c", "Ej. 6", 0, 0, true)),
            "Solución x₁",
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
        double b = Guards.requireFinite(in.b(), "b", "Coeficiente b");
        double c = Guards.requireFinite(in.c(), "c", "Termino c");

        if (a == 0) {
            if (b == 0) {
                if (c == 0) {
                    throw new CalcException(ErrorCode.INFINITE_SOLUTIONS,
                            "0 = 0: infinitas soluciones.");
                }
                throw new CalcException(ErrorCode.NO_SOLUTION,
                        "0 = " + c + ": sin solucion.");
            }
            double x = Guards.requireFiniteResult(-c / b);
            List<String> linSteps = List.of(
                    "Aviso: a = 0, no es cuadratica; se resuelve como lineal.",
                    "Ecuacion: " + b + "x + (" + c + ") = 0",
                    "Resultado: x = " + x);
            return new CalculationResult(x, "Solución x", "x = -c/b (lineal)",
                    linSteps, "", true, "a = 0: ecuacion lineal, no cuadratica.");
        }

        double d = b * b - 4 * a * c;
        d = Guards.requireFiniteResult(d);
        List<String> steps = new ArrayList<>();
        steps.add("Ecuacion: " + a + "x^2 + (" + b + ")x + (" + c + ") = 0");
        steps.add("Discriminante: D = b^2 - 4ac = " + d);
        if (d < 0) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "a",
                    "D = " + d + " < 0: sin soluciones reales (raices complejas conjugadas).");
        }
        if (d == 0) {
            double x = Guards.requireFiniteResult(-b / (2 * a));
            steps.add("D = 0: raiz doble x = -b/2a = " + x);
            steps.add("Factorizacion: " + a + "(x - (" + x + "))^2 = 0");
            return new CalculationResult(x, "Solución x (doble)",
                    "x = -b/2a", List.copyOf(steps), "", true, null);
        }
        double sqrtD = Math.sqrt(d);
        double x1 = Guards.requireFiniteResult((-b + sqrtD) / (2 * a));
        double x2 = Guards.requireFiniteResult((-b - sqrtD) / (2 * a));
        steps.add("Formula: x = (-b ± √D) / 2a");
        steps.add("x1 = (" + (-b) + " + " + sqrtD + ") / " + (2 * a) + " = " + x1);
        steps.add("x2 = (" + (-b) + " - " + sqrtD + ") / " + (2 * a) + " = " + x2);
        steps.add("Factorizacion: " + a + "(x - (" + x1 + "))(x - (" + x2 + ")) = 0");
        return new CalculationResult(x1, "Solución x₁",
                "x = (-b ± √D) / 2a", List.copyOf(steps), "", true, null);
    }
}
