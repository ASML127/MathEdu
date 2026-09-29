package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Rectangle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del rectangulo: A = base * altura. */
public class RectangleAreaCalculator implements Calculator<RectangleAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.rectangle.area";

    public record Input(double width, double height, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Rectangulo — Area",
            "Geometria plana",
            "A = base * altura",
            List.of(
                    InputDef.positive("width", "Base (b)", "Ej. 5"),
                    InputDef.positive("height", "Altura (h)", "Ej. 3")),
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
        double b = Guards.requirePositive(in.width(), "width", "Base");
        double h = Guards.requirePositive(in.height(), "height", "Altura");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(new Rectangle(b, h).area());
        List<String> steps = List.of(
                "Formula: A = base * altura",
                "Sustitucion: A = " + b + " * " + h,
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = base * altura", steps, unit.areaSymbol(), true, null);
    }
}
