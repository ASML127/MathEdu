package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.RightTriangle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del triangulo rectangulo: A = cateto1 * cateto2 / 2. */
public class RightAreaCalculator implements Calculator<RightAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.right.area";

    public record Input(double legA, double legB, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo rectangulo — Area",
            "Geometria plana",
            "A = cateto1 * cateto2 / 2",
            List.of(
                    InputDef.positive("legA", "Cateto a", "Ej. 3"),
                    InputDef.positive("legB", "Cateto b", "Ej. 4")),
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
        double a = Guards.requirePositive(in.legA(), "legA", "Cateto a");
        double b = Guards.requirePositive(in.legB(), "legB", "Cateto b");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double area = Guards.requireFiniteResult(new RightTriangle(a, b).area());
        List<String> steps = List.of(
                "Formula: A = cateto1 * cateto2 / 2",
                "Sustitucion: A = " + a + " * " + b + " / 2",
                "Resultado: A = " + area + " " + unit.areaSymbol());
        return new CalculationResult(area, "Area", "A = cateto1 * cateto2 / 2", steps,
                unit.areaSymbol(), true, null);
    }
}
