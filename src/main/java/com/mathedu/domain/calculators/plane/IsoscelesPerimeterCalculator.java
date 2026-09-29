package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.IsoscelesTriangle;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Perimetro del triangulo isosceles: P = base + 2*lado. */
public class IsoscelesPerimeterCalculator implements Calculator<IsoscelesPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.isosceles.perimeter";

    public record Input(double base, double equalSide, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Triangulo isosceles — Perimetro",
            "Geometria plana",
            "P = base + 2 * lado",
            List.of(
                    InputDef.positive("base", "Base (b)", "Ej. 8"),
                    InputDef.positive("equalSide", "Lado igual (l)", "Ej. 6")),
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
        double b = Guards.requirePositive(in.base(), "base", "Base");
        double l = Guards.requirePositive(in.equalSide(), "equalSide", "Lado igual");
        if (2 * l <= b) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "base",
                    "La base no puede superar la suma de los dos lados iguales (2*" + l + " <= " + b + ").");
        }
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        IsoscelesTriangle t = new IsoscelesTriangle(b, l);
        double p = Guards.requireFiniteResult(t.perimeter());
        List<String> steps = List.of(
                "Formula: P = base + 2 * lado",
                "Sustitucion: P = " + b + " + 2 * " + l,
                "Altura deducida: h = " + t.height() + " " + unit.getSymbol(),
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = base + 2 * lado", steps,
                unit.getSymbol(), true, null);
    }
}
