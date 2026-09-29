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

/** Area del poligono regular: A = n*s^2 / (4*tan(pi/n)). */
public class RegularPolygonAreaCalculator
        implements Calculator<RegularPolygonAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.polygon.area";

    public record Input(int sides, double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Poligono regular — Area",
            "Geometria plana",
            "A = n * s^2 / (4 * tan(pi/n))",
            List.of(
                    new InputDef("n", "N.º de lados (n)", "Ej. 6", 3, 1_000_000, true),
                    InputDef.positive("side", "Lado (s)", "Ej. 4")),
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
        int n = Guards.requireSides(in.sides());
        double s = Guards.requirePositive(in.side(), "side", "Lado");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        RegularPolygon poly = new RegularPolygon(n, s);
        double a = Guards.requireFiniteResult(poly.area());
        List<String> steps = List.of(
                "Formula: A = n * s^2 / (4 * tan(pi/n))",
                "Apotema: ap = s / (2*tan(pi/n)) = " + poly.apothem() + " " + unit.getSymbol(),
                "Sustitucion: A = " + n + " * (" + s + ")^2 / (4 * tan(pi/" + n + "))",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = n * s^2 / (4 * tan(pi/n))", steps,
                unit.areaSymbol(), true, null);
    }
}
