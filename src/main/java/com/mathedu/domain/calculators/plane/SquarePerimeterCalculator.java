package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Square;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del cuadrado: P = 4*l. */
public class SquarePerimeterCalculator implements Calculator<SquarePerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.square.perimeter";

    public record Input(double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Cuadrado — Perimetro",
            "Geometria plana",
            "P = 4 * l",
            List.of(InputDef.positive("side", "Lado (l)", "Ej. 4")),
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
        double p = Guards.requireFiniteResult(new Square(s).perimeter());
        List<String> steps = List.of(
                "Formula: P = 4 * l",
                "Sustitucion: P = 4 * " + s,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 4 * l", steps, unit.getSymbol(), true, null);
    }
}
