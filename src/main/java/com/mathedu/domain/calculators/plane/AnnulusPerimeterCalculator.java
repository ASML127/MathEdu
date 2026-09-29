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

/** Perimetro de la corona (suma de circunferencias): P = 2*pi*(R + r). */
public class AnnulusPerimeterCalculator
        implements Calculator<AnnulusPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.annulus.perimeter";

    public record Input(double outerRadius, double innerRadius, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Corona circular — Perimetro",
            "Geometria plana",
            "P = 2 * pi * (R + r)",
            List.of(
                    InputDef.positive("R", "Radio exterior (R)", "Ej. 5"),
                    InputDef.positive("r", "Radio interior (r)", "Ej. 3")),
            "Perimetro",
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
        double big = Guards.requirePositive(in.outerRadius(), "R", "Radio exterior");
        double small = Guards.requirePositive(in.innerRadius(), "r", "Radio interior");
        Guards.requireOuterGreaterThanInner(big, small);
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(new Annulus(big, small).perimeter());
        List<String> steps = List.of(
                "Formula: P = 2 * pi * (R + r)",
                "Sustitucion: P = 2 * pi * (" + big + " + " + small + ")",
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 2 * pi * (R + r)", steps,
                unit.getSymbol(), true, null);
    }
}
