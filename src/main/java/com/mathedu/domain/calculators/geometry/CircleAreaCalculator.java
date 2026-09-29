package com.mathedu.domain.calculators.geometry;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Circle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/**
 * Area del circulo A = pi * r^2 (refactorizada en Fase 1 sobre {@link Circle}).
 */
public class CircleAreaCalculator implements Calculator<CircleAreaInput> {

    public static final String FORMULA_ID = "plane.circle.area";

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Circulo — Area",
            "Geometria plana",
            "A = pi * r^2",
            List.of(InputDef.positive("r", "Radio (r)", "Ej. 3")),
            "Area",
            "unidad^2");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(CircleAreaInput input) throws CalcException {
        if (input == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double r = Guards.requirePositive(input.radius(), "r", "Radio");
        LengthUnit unit = input.unit() == null ? LengthUnit.CM : input.unit();
        double area = Guards.requireFiniteResult(new Circle(r).area());
        List<String> steps = List.of(
                "Formula: A = pi * r^2",
                "Sustitucion: A = pi * (" + r + ")^2",
                "Resultado: A = " + area + " " + unit.areaSymbol());
        return new CalculationResult(area, "Area", "A = pi * r^2", steps, unit.areaSymbol(), true, null);
    }
}
