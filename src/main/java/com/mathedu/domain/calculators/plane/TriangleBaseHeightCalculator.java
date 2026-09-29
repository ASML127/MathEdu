package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Triangle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del triangulo con base y altura: A = base*altura/2. */
public class TriangleBaseHeightCalculator implements Calculator<TriangleBaseHeightCalculator.Input> {

    public static final String FORMULA_ID = "plane.triangle.area.base-height";

    public record Input(double base, double height, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo — Area (base y altura)",
            "Geometria plana",
            "A = base * altura / 2",
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
        double a = Guards.requireFiniteResult(Triangle.areaBaseHeight(b, h));
        List<String> steps = List.of(
                "Formula: A = base * altura / 2",
                "Sustitucion: A = " + b + " * " + h + " / 2",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = base * altura / 2", steps,
                unit.areaSymbol(), true, null);
    }
}
