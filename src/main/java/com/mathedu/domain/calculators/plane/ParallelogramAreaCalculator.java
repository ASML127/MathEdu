package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Parallelogram;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del paralelogramo: A = base * altura. */
public class ParallelogramAreaCalculator implements Calculator<ParallelogramAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.parallelogram.area";

    public record Input(double base, double height, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Paralelogramo — Area",
            "Geometria plana",
            "A = base * altura",
            List.of(
                    InputDef.positive("base", "Base (b)", "Ej. 6"),
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
        double b = Guards.requirePositive(in.base(), "base", "Base");
        double h = Guards.requirePositive(in.height(), "height", "Altura");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(Parallelogram.area(b, h));
        List<String> steps = List.of(
                "Formula: A = base * altura",
                "Sustitucion: A = " + b + " * " + h,
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = base * altura", steps,
                unit.areaSymbol(), true, null);
    }
}
