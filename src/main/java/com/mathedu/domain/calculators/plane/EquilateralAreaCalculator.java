package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.EquilateralTriangle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del triangulo equilatero: A = sqrt(3)/4 * l^2. */
public class EquilateralAreaCalculator implements Calculator<EquilateralAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.equilateral.area";

    public record Input(double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo equilatero — Area",
            "Geometria plana",
            "A = sqrt(3)/4 * l^2",
            List.of(InputDef.positive("side", "Lado (l)", "Ej. 6")),
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
        double s = Guards.requirePositive(in.side(), "side", "Lado");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        EquilateralTriangle t = new EquilateralTriangle(s);
        double a = Guards.requireFiniteResult(t.area());
        List<String> steps = List.of(
                "Formula: A = sqrt(3)/4 * l^2",
                "Altura: h = l*sqrt(3)/2 = " + t.height() + " " + unit.getSymbol(),
                "Sustitucion: A = sqrt(3)/4 * (" + s + ")^2",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = sqrt(3)/4 * l^2", steps,
                unit.areaSymbol(), true, null);
    }
}
