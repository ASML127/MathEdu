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

/** Perimetro del paralelogramo: P = 2*(base + lado). */
public class ParallelogramPerimeterCalculator
        implements Calculator<ParallelogramPerimeterCalculator.Input> {

    public static final String FORMULA_ID = "plane.parallelogram.perimeter";

    public record Input(double base, double side, double height, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Paralelogramo — Perimetro",
            "Geometria plana",
            "P = 2 * (base + lado)",
            List.of(
                    InputDef.positive("base", "Base (b)", "Ej. 6"),
                    InputDef.positive("side", "Lado (a)", "Ej. 5"),
                    InputDef.positive("height", "Altura (h)", "Ej. 4")),
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
        double s = Guards.requirePositive(in.side(), "side", "Lado");
        double h = Guards.requirePositive(in.height(), "height", "Altura");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        // La altura debe ser coherente: no puede superar al lado oblicuo.
        if (h > s) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "height",
                    "La altura (" + h + ") no puede superar al lado oblicuo (" + s + ").");
        }
        double p = Guards.requireFiniteResult(new Parallelogram(b, s, h).perimeter());
        List<String> steps = List.of(
                "Formula: P = 2 * (base + lado)",
                "Sustitucion: P = 2 * (" + b + " + " + s + ")",
                "Resultado: P = " + p + " " + unit.getSymbol());
        return new CalculationResult(p, "Perimetro", "P = 2 * (base + lado)", steps,
                unit.getSymbol(), true, null);
    }
}
