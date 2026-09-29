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

/** Perimetro del triangulo: P = a + b + c. */
public class TrianglePerimeterCalculator implements Calculator<TrianglePerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.triangle.perimeter";

    public record Input(double a, double b, double c, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo — Perimetro",
            "Geometria plana",
            "P = a + b + c",
            List.of(
                    InputDef.positive("a", "Lado a", "Ej. 3"),
                    InputDef.positive("b", "Lado b", "Ej. 4"),
                    InputDef.positive("c", "Lado c", "Ej. 5")),
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
        double a = Guards.requirePositive(in.a(), "a", "Lado a");
        double b = Guards.requirePositive(in.b(), "b", "Lado b");
        double c = Guards.requirePositive(in.c(), "c", "Lado c");
        Guards.requireTriangleInequality(a, b, c);
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double p = Guards.requireFiniteResult(new Triangle(a, b, c).perimeter());
        List<String> steps = List.of(
                "Formula: P = a + b + c",
                "Sustitucion: P = " + a + " + " + b + " + " + c,
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = a + b + c", steps,
                unit.getSymbol(), true, null);
    }
}
