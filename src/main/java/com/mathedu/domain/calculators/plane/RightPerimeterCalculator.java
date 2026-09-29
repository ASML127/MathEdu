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

/** Perimetro del triangulo rectangulo (hipotenusa por Pitagoras). */
public class RightPerimeterCalculator implements Calculator<RightPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.right.perimeter";

    public record Input(double legA, double legB, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo rectangulo — Perimetro",
            "Geometria plana",
            "P = a + b + sqrt(a^2 + b^2)",
            List.of(
                    InputDef.positive("legA", "Cateto a", "Ej. 3"),
                    InputDef.positive("legB", "Cateto b", "Ej. 4")),
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
        double a = Guards.requirePositive(in.legA(), "legA", "Cateto a");
        double b = Guards.requirePositive(in.legB(), "legB", "Cateto b");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        RightTriangle t = new RightTriangle(a, b);
        double p = Guards.requireFiniteResult(t.perimeter());
        List<String> steps = List.of(
                "Teorema de Pitagoras: c = sqrt(a^2 + b^2) = " + t.hypotenuse() + " " + unit.getSymbol(),
                "Formula: P = a + b + c",
                "Sustitucion: P = " + a + " + " + b + " + " + t.hypotenuse(),
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = a + b + sqrt(a^2 + b^2)", steps,
                unit.getSymbol(), true, null);
    }
}
