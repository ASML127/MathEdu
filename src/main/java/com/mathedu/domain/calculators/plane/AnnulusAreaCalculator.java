package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Annulus;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area de la corona circular: A = pi*(R^2 - r^2). */
public class AnnulusAreaCalculator implements Calculator<AnnulusAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.annulus.area";

    public record Input(double outerRadius, double innerRadius, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Corona circular — Area",
            "Geometria plana",
            "A = pi * (R^2 - r^2)",
            List.of(
                    InputDef.positive("R", "Radio exterior (R)", "Ej. 5"),
                    InputDef.positive("r", "Radio interior (r)", "Ej. 3")),
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
        double big = Guards.requirePositive(in.outerRadius(), "R", "Radio exterior");
        double small = Guards.requirePositive(in.innerRadius(), "r", "Radio interior");
        Guards.requireOuterGreaterThanInner(big, small);
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(new Annulus(big, small).area());
        List<String> steps = List.of(
                "Formula: A = pi * (R^2 - r^2)",
                "Sustitucion: A = pi * ((" + big + ")^2 - (" + small + ")^2)",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = pi * (R^2 - r^2)", steps,
                unit.areaSymbol(), true, null);
    }
}
