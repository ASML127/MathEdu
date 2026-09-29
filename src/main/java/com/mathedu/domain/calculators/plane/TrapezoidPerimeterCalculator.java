package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Trapezoid;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del trapecio: P = B + b + c + d. */
public class TrapezoidPerimeterCalculator
        implements Calculator<TrapezoidPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.trapezoid.perimeter";

    public record Input(double bigBase, double smallBase, double sideC, double sideD,
                        LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Trapecio — Perimetro",
            "Geometria plana",
            "P = B + b + c + d",
            List.of(
                    InputDef.positive("bigBase", "Base mayor (B)", "Ej. 10"),
                    InputDef.positive("smallBase", "Base menor (b)", "Ej. 6"),
                    InputDef.positive("sideC", "Lado c", "Ej. 5"),
                    InputDef.positive("sideD", "Lado d", "Ej. 5")),
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
        double big = Guards.requirePositive(in.bigBase(), "bigBase", "Base mayor");
        double small = Guards.requirePositive(in.smallBase(), "smallBase", "Base menor");
        double c = Guards.requirePositive(in.sideC(), "sideC", "Lado c");
        double d = Guards.requirePositive(in.sideD(), "sideD", "Lado d");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(
                new Trapezoid(big, small, c, d).perimeter());
        List<String> steps = List.of(
                "Formula: P = B + b + c + d",
                "Sustitucion: P = " + big + " + " + small + " + " + c + " + " + d,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = B + b + c + d", steps,
                unit.getSymbol(), true, null);
    }
}
