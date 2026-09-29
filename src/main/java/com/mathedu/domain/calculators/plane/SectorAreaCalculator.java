package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.CircularSector;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del sector circular: A = pi*r^2*theta/360 (theta en grados). */
public class SectorAreaCalculator implements Calculator<SectorAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.sector.area";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double radius, double angleDeg, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Sector circular — Area",
            "Geometria plana",
            "A = pi * r^2 * theta / 360",
            List.of(
                    InputDef.positive("r", "Radio (r)", "Ej. 5"),
                    InputDef.positive("angle", "Angulo (theta)", "Ej. 90")),
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
        double theta = Guards.requireAngleDeg(in.angleDeg(), "angle");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(new CircularSector(r, theta).area());
        List<String> steps = List.of(
                "Formula: A = pi * r^2 * theta / 360",
                "Sustitucion: A = pi * (" + r + ")^2 * " + theta + " / 360",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = pi * r^2 * theta / 360", steps,
                unit.areaSymbol(), true, null);
    }
}
