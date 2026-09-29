package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Circle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del circulo (circunferencia): P = 2*pi*r. */
public class CirclePerimeterCalculator implements Calculator<CirclePerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.circle.perimeter";

    public record Input(double radius, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Circulo — Perimetro",
            "Geometria plana",
            "P = 2 * pi * r",
            List.of(InputDef.positive("r", "Radio (r)", "Ej. 3")),
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
        double p = Guards.requireFiniteResult(new Circle(r).perimeter());
        List<String> steps = List.of(
                "Formula: P = 2 * pi * r",
                "Sustitucion: P = 2 * pi * " + r,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 2 * pi * r", steps, unit.getSymbol(), true, null);
    }
}
