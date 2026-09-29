package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.trig.TriangleSolver;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/**
 * Resolucion ASA: angulos A, B y lado comprendido c.
 * El valor principal es el lado a; b y C van en los pasos.
 */
public class SolveAsaCalculator implements Calculator<SolveAsaCalculator.Input> {

    public static final String FORMULA_ID = "trig.solve-asa";

    /** Angulos siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double angleADeg, double angleBDeg, double c,
                        LengthUnit unit, AngleUnit angleUnit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Resolver triángulo (ASA)",
            "Trigonometría",
            "C = 180° - A - B ; a = c·sin A / sin C",
            List.of(
                    InputDef.positive("angleA", "Ángulo A", "Ej. 50"),
                    InputDef.positive("angleB", "Ángulo B", "Ej. 60"),
                    InputDef.positive("c", "Lado c (comprendido)", "Ej. 8")),
            "Lado a",
            "unidad");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        TriangleSolver.Solution s = TriangleSolver.asa(
                Guards.requireFinite(in.angleADeg(), "angleA", "Angulo A"),
                Guards.requireFinite(in.angleBDeg(), "angleB", "Angulo B"),
                Guards.requirePositive(in.c(), "c", "Lado c"));
        AngleUnit au = in.angleUnit() == null ? AngleUnit.DEG : in.angleUnit();
        LengthUnit lu = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(s.a());
        List<String> steps = List.of(
                "Tercer angulo: C = 180° - A - B = " + TrigFormat.angle(s.C(), au),
                "Teorema del seno: a/sin A = c/sin C",
                "a = " + s.c() + "·sin(" + TrigFormat.angle(s.A(), au) + ")/sin("
                        + TrigFormat.angle(s.C(), au) + ") = " + a + " " + lu.getSymbol(),
                "b = " + s.b() + " " + lu.getSymbol());
        return new CalculationResult(a, "Lado a",
                "a = c·sin A / sin C", steps, lu.getSymbol(), true, null);
    }
}
