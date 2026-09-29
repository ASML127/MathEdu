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
 * Sistema 2x2 por Cramer. El valor principal es x; y va en los pasos.
 */
public class System2x2Calculator implements Calculator<System2x2Calculator.Input> {

    public static final String FORMULA_ID = "arith.system2x2";

    public record Input(double a1, double b1, double c1, double a2, double b2, double c2) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Sistema de 2 ecuaciones (Cramer)",
            "Aritmética y álgebra",
            "a1·x + b1·y = c1 ; a2·x + b2·y = c2",
            List.of(
                    new InputDef("a1", "a₁", "Ej. 2", 0, 0, true),
                    new InputDef("b1", "b₁", "Ej. 3", 0, 0, true),
                    new InputDef("c1", "c₁", "Ej. 7", 0, 0, true),
                    new InputDef("a2", "a₂", "Ej. 1", 0, 0, true),
                    new InputDef("b2", "b₂", "Ej. -1", 0, 0, true),
                    new InputDef("c2", "c₂", "Ej. 1", 0, 0, true)),
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
        double[] v = {
                Guards.requireFinite(in.a1(), "a1", "a1"),
                Guards.requireFinite(in.b1(), "b1", "b1"),
                Guards.requireFinite(in.c1(), "c1", "c1"),
                Guards.requireFinite(in.a2(), "a2", "a2"),
                Guards.requireFinite(in.b2(), "b2", "b2"),
                Guards.requireFinite(in.c2(), "c2", "c2")};
        LinearSystems.Solution2 sol =
                LinearSystems.solve2x2(v[0], v[1], v[2], v[3], v[4], v[5]);
        Guards.requireFiniteResult(sol.x());
        Guards.requireFiniteResult(sol.y());
        List<String> steps = new ArrayList<>(sol.steps());
        steps.add("Solucion: x = " + sol.x() + ", y = " + sol.y());
        return new CalculationResult(sol.x(), "Solución x",
                "Cramer: x = Dx/D, y = Dy/D", List.copyOf(steps), "", true, null);
    }
}
