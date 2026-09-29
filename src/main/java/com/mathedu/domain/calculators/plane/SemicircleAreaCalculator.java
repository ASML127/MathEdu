package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Semicircle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del semicirculo: A = pi*r^2/2. */
public class SemicircleAreaCalculator implements Calculator<SemicircleAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.semicircle.area";

    public record Input(double radius, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Semicirculo — Area",
            "Geometria plana",
            "A = pi * r^2 / 2",
            List.of(InputDef.positive("r", "Radio (r)", "Ej. 4")),
            "Area",
            "unidad^2");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double r = Guards.requirePositive(in.radius(), "r", "Radio");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(new Semicircle(r).area());
        List<String> steps = List.of(
                "Formula: A = pi * r^2 / 2",
                "Sustitucion: A = pi * (" + r + ")^2 / 2",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = pi * r^2 / 2", steps,
                unit.areaSymbol(), true, null);
    }
}
