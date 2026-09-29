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

/** Area del triangulo por Heron conociendo los 3 lados. */
public class TriangleHeronCalculator implements Calculator<TriangleHeronCalculator.Input> {

    public static final String FORMULA_ID = "plane.triangle.area.heron";

    public record Input(double a, double b, double c, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo — Area (Heron, 3 lados)",
            "Geometria plana",
            "A = sqrt(s*(s-a)*(s-b)*(s-c)), s = (a+b+c)/2",
            List.of(
                    InputDef.positive("a", "Lado a", "Ej. 3"),
                    InputDef.positive("b", "Lado b", "Ej. 4"),
                    InputDef.positive("c", "Lado c", "Ej. 5")),
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
        double a = Guards.requirePositive(in.a(), "a", "Lado a");
        double b = Guards.requirePositive(in.b(), "b", "Lado b");
        double c = Guards.requirePositive(in.c(), "c", "Lado c");
        Guards.requireTriangleInequality(a, b, c);
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        Triangle t = new Triangle(a, b, c);
        double s = t.perimeter() / 2.0;
        double area = Guards.requireFiniteResult(t.area());
        List<String> steps = List.of(
                "Formula: A = sqrt(s*(s-a)*(s-b)*(s-c)) con s = (a+b+c)/2",
                "Semiperimetro: s = (" + a + " + " + b + " + " + c + ") / 2 = " + s,
                "Resultado: A = " + area + " " + unit.areaSymbol());
        return new CalculationResult(area, "Area", "A = sqrt(s*(s-a)*(s-b)*(s-c))", steps,
                unit.areaSymbol(), true, null);
    }
}
