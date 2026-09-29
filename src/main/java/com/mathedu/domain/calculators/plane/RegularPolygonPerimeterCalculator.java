package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.RegularPolygon;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del poligono regular: P = n*s. */
public class RegularPolygonPerimeterCalculator
        implements Calculator<RegularPolygonPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.polygon.perimeter";

    public record Input(int sides, double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Poligono regular — Perimetro",
            "Geometria plana",
            "P = n * s",
            List.of(
                    new InputDef("n", "N.º de lados (n)", "Ej. 6", 3, 1_000_000, true),
                    InputDef.positive("side", "Lado (s)", "Ej. 4")),
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
        int n = Guards.requireSides(in.sides());
        double s = Guards.requirePositive(in.side(), "side", "Lado");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(new RegularPolygon(n, s).perimeter());
        List<String> steps = List.of(
                "Formula: P = n * s",
                "Sustitucion: P = " + n + " * " + s,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = n * s", steps,
                unit.getSymbol(), true, null);
    }
}
