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

/** Perimetro del cometa/deltoide: P = 2*(a + b). */
public class KitePerimeterCalculator implements Calculator<KitePerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.kite.perimeter";

    public record Input(double sideA, double sideB, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Cometa — Perimetro",
            "Geometria plana",
            "P = 2 * (a + b)",
            List.of(
                    InputDef.positive("sideA", "Lado a", "Ej. 5"),
                    InputDef.positive("sideB", "Lado b", "Ej. 7")),
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
        double a = Guards.requirePositive(in.sideA(), "sideA", "Lado a");
        double b = Guards.requirePositive(in.sideB(), "sideB", "Lado b");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(2 * (a + b));
        List<String> steps = List.of(
                "Formula: P = 2 * (a + b)",
                "Sustitucion: P = 2 * (" + a + " + " + b + ")",
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 2 * (a + b)", steps,
                unit.getSymbol(), true, null);
    }
}
