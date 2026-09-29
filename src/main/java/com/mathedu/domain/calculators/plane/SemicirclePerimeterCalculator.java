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

/** Perimetro del semicirculo: P = pi*r + 2*r (arco + diametro). */
public class SemicirclePerimeterCalculator
        implements Calculator<SemicirclePerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.semicircle.perimeter";

    public record Input(double radius, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Semicirculo — Perimetro",
            "Geometria plana",
            "P = pi * r + 2 * r",
            List.of(InputDef.positive("r", "Radio (r)", "Ej. 4")),
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
        double r = Guards.requirePositive(in.radius(), "r", "Radio");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        Semicircle s = new Semicircle(r);
        double p = Guards.requireFiniteResult(s.perimeter());
        List<String> steps = List.of(
                "Formula: P = pi * r + 2 * r (arco + diametro)",
                "Sustitucion: P = pi * " + r + " + 2 * " + r,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = pi * r + 2 * r", steps,
                unit.getSymbol(), true, null);
    }
}
