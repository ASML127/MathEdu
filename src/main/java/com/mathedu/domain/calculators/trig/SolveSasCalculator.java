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
 * Resolucion SAS: lados a, b y angulo comprendido C.
 * El valor principal es el lado c; A y B van en los pasos.
 */
public class SolveSasCalculator implements Calculator<SolveSasCalculator.Input> {

    public static final String FORMULA_ID = "trig.solve-sas";

    /** Angulo C siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double a, double b, double angleCDeg, LengthUnit unit, AngleUnit angleUnit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Resolver triángulo (SAS)",
            "Trigonometría",
            "c² = a² + b² - 2ab·cos C",
            List.of(
                    InputDef.positive("a", "Lado a", "Ej. 5"),
                    InputDef.positive("b", "Lado b", "Ej. 7"),
                    InputDef.positive("angleC", "Ángulo C (comprendido)", "Ej. 60")),
            "Lado c",
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
        TriangleSolver.Solution s = TriangleSolver.sas(
                Guards.requirePositive(in.a(), "a", "Lado a"),
                Guards.requirePositive(in.b(), "b", "Lado b"),
                Guards.requireFinite(in.angleCDeg(), "angleC", "Angulo C"));
        AngleUnit au = in.angleUnit() == null ? AngleUnit.DEG : in.angleUnit();
        LengthUnit lu = in.unit() == null ? LengthUnit.CM : in.unit();
        double c = Guards.requireFiniteResult(s.c());
        List<String> steps = List.of(
                "Teorema del coseno: c² = a² + b² - 2ab·cos C",
                "Sustitucion: c² = " + s.a() + "² + " + s.b() + "² - 2·" + s.a() + "·" + s.b()
                        + "·cos(" + TrigFormat.angle(s.C(), au) + ")",
                "Lado c = " + c + " " + lu.getSymbol(),
                "Teorema del seno: A = " + TrigFormat.angle(s.A(), au),
                "B = 180° - A - C = " + TrigFormat.angle(s.B(), au));
        return new CalculationResult(c, "Lado c",
                "c² = a² + b² - 2ab·cos C", steps, lu.getSymbol(), true, null);
    }
}
