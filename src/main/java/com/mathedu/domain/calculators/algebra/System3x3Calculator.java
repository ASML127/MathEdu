package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.algebra.LinearSystems;
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
 * Sistema 3x3 por eliminacion de Gauss con pivoteo parcial.
 * El valor principal es x; y, z van en los pasos.
 */
public class System3x3Calculator implements Calculator<System3x3Calculator.Input> {

    public static final String FORMULA_ID = "arith.system3x3";

    public record Input(
            double a1, double b1, double c1, double d1,
            double a2, double b2, double c2, double d2,
            double a3, double b3, double c3, double d3) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Sistema de 3 ecuaciones (Gauss)",
            "Aritmética y álgebra",
            "aᵢ·x + bᵢ·y + cᵢ·z = dᵢ  (i = 1,2,3)",
            List.of(
                    new InputDef("a1", "a₁", "Ej. 1", 0, 0, true),
                    new InputDef("b1", "b₁", "Ej. 1", 0, 0, true),
                    new InputDef("c1", "c₁", "Ej. 1", 0, 0, true),
                    new InputDef("d1", "d₁", "Ej. 6", 0, 0, true),
                    new InputDef("a2", "a₂", "Ej. 0", 0, 0, true),
                    new InputDef("b2", "b₂", "Ej. 2", 0, 0, true),
                    new InputDef("c2", "c₂", "Ej. 5", 0, 0, true),
                    new InputDef("d2", "d₂", "Ej. -4", 0, 0, true),
                    new InputDef("a3", "a₃", "Ej. 2", 0, 0, true),
                    new InputDef("b3", "b₃", "Ej. 5", 0, 0, true),
                    new InputDef("c3", "c₃", "Ej. -1", 0, 0, true),
                    new InputDef("d3", "d₃", "Ej. 27", 0, 0, true)),
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
        double[][] m = {
                {f(in.a1(), "a1"), f(in.b1(), "b1"), f(in.c1(), "c1"), f(in.d1(), "d1")},
                {f(in.a2(), "a2"), f(in.b2(), "b2"), f(in.c2(), "c2"), f(in.d2(), "d2")},
                {f(in.a3(), "a3"), f(in.b3(), "b3"), f(in.c3(), "c3"), f(in.d3(), "d3")}};
        LinearSystems.Solution3 sol = LinearSystems.solve3x3(m);
        Guards.requireFiniteResult(sol.x());
        Guards.requireFiniteResult(sol.y());
        Guards.requireFiniteResult(sol.z());
        List<String> steps = new ArrayList<>(sol.steps());
        steps.add("Solucion: x = " + sol.x() + ", y = " + sol.y() + ", z = " + sol.z());
        return new CalculationResult(sol.x(), "Solución x",
                "Gauss: triangular + sustitucion regresiva", List.copyOf(steps), "", true, null);
    }

    private static double f(double v, String field) throws CalcException {
        return Guards.requireFinite(v, field, field);
    }
}
