package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del rombo: P = 4*lado. */
public class RhombusPerimeterCalculator implements Calculator<RhombusPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.rhombus.perimeter";

    public record Input(double side, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Rombo — Perimetro",
            "Geometria plana",
            "P = 4 * lado",
            List.of(InputDef.positive("side", "Lado (l)", "Ej. 5")),
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
        // El perimetro solo depende del lado: P = 4*l.
        double p = Guards.requireFiniteResult(4 * s);
        List<String> steps = List.of(
                "Formula: P = 4 * lado",
                "Sustitucion: P = 4 * " + s,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 4 * lado", steps,
                unit.getSymbol(), true, null);
    }
}
