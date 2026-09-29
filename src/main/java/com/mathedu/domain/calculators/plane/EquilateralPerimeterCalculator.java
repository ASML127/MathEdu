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

/** Perimetro del triangulo equilatero: P = 3*l. */
public class EquilateralPerimeterCalculator implements Calculator<EquilateralPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.equilateral.perimeter";

    public record Input(double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo equilatero — Perimetro",
            "Geometria plana",
            "P = 3 * l",
            List.of(InputDef.positive("side", "Lado (l)", "Ej. 6")),
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
        double s = Guards.requirePositive(in.side(), "side", "Lado");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(new EquilateralTriangle(s).perimeter());
        List<String> steps = List.of(
                "Formula: P = 3 * l",
                "Sustitucion: P = 3 * " + s,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 3 * l", steps,
                unit.getSymbol(), true, null);
    }
}
