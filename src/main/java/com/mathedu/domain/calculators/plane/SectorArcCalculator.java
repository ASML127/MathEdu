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

/** Longitud del arco de un sector: L = 2*pi*r*theta/360 (theta en grados). */
public class SectorArcCalculator implements Calculator<SectorArcCalculator.Input> {

    public static final String FORMULA_ID = "plane.sector.arc";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double radius, double angleDeg, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Sector circular — Arco",
            "Geometria plana",
            "L = 2 * pi * r * theta / 360",
            List.of(
                    InputDef.positive("r", "Radio (r)", "Ej. 5"),
                    InputDef.positive("angle", "Angulo (theta)", "Ej. 90")),
            "Longitud del arco",
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
        double r = Guards.requirePositive(in.radius(), "r", "Radio");
        double theta = Guards.requireAngleDeg(in.angleDeg(), "angle");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double l = Guards.requireFiniteResult(new CircularSector(r, theta).arcLength());
        List<String> steps = List.of(
                "Formula: L = 2 * pi * r * theta / 360",
                "Sustitucion: L = 2 * pi * " + r + " * " + theta + " / 360",
                "Resultado: L = " + l + " " + unit.getSymbol());
        return new CalculationResult(l, "Longitud del arco", "L = 2 * pi * r * theta / 360",
                steps, unit.getSymbol(), true, null);
    }
}
