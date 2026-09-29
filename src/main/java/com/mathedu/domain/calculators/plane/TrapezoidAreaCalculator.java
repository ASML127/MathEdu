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

/** Area del trapecio: A = (B + b)*h/2. */
public class TrapezoidAreaCalculator implements Calculator<TrapezoidAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.trapezoid.area";

    public record Input(double bigBase, double smallBase, double height, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Trapecio — Area",
            "Geometria plana",
            "A = (B + b) * h / 2",
            List.of(
                    InputDef.positive("bigBase", "Base mayor (B)", "Ej. 10"),
                    InputDef.positive("smallBase", "Base menor (b)", "Ej. 6"),
                    InputDef.positive("height", "Altura (h)", "Ej. 4")),
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
        double big = Guards.requirePositive(in.bigBase(), "bigBase", "Base mayor");
        double small = Guards.requirePositive(in.smallBase(), "smallBase", "Base menor");
        double h = Guards.requirePositive(in.height(), "height", "Altura");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(Trapezoid.area(big, small, h));
        List<String> steps = List.of(
                "Formula: A = (B + b) * h / 2",
                "Sustitucion: A = (" + big + " + " + small + ") * " + h + " / 2",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = (B + b) * h / 2", steps,
                unit.areaSymbol(), true, null);
    }
}
