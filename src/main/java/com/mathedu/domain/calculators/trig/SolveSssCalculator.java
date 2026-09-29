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
 * Resolucion SSS por teorema del coseno. El valor principal es el
 * angulo A (en la unidad seleccionada); B y C van en los pasos.
 */
public class SolveSssCalculator implements Calculator<SolveSssCalculator.Input> {

    public static final String FORMULA_ID = "trig.solve-sss";

    public record Input(double a, double b, double c, LengthUnit unit, AngleUnit angleUnit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Resolver triángulo (SSS)",
            "Trigonometría",
            "cos A = (b² + c² - a²) / 2bc  (y ciclicas)",
            List.of(
                    InputDef.positive("a", "Lado a", "Ej. 7"),
                    InputDef.positive("b", "Lado b", "Ej. 5"),
                    InputDef.positive("c", "Lado c", "Ej. 6")),
            "Ángulo A",
            "°/rad");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        TriangleSolver.Solution s = TriangleSolver.sss(
                Guards.requirePositive(in.a(), "a", "Lado a"),
                Guards.requirePositive(in.b(), "b", "Lado b"),
                Guards.requirePositive(in.c(), "c", "Lado c"));
        AngleUnit au = in.angleUnit() == null ? AngleUnit.DEG : in.angleUnit();
        LengthUnit lu = in.unit() == null ? LengthUnit.CM : in.unit();
        double aOut = Guards.requireFiniteResult(au.fromDegrees(s.A()));
        List<String> steps = List.of(
                "Teorema del coseno: A = arccos((b² + c² - a²)/2bc)",
                "Lados (" + lu.getSymbol() + "): a = " + s.a() + ", b = " + s.b() + ", c = " + s.c(),
                "A = " + TrigFormat.angle(s.A(), au),
                "B = " + TrigFormat.angle(s.B(), au),
                "C = " + TrigFormat.angle(s.C(), au),
                "Verificacion: A + B + C = 180°");
        return new CalculationResult(aOut, "Ángulo A",
                "cos A = (b² + c² - a²) / 2bc", steps, au.getSymbol(), true, null);
    }
}
