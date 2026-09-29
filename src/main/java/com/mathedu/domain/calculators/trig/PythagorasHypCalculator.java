package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Teorema de Pitagoras: hipotenusa c = √(a² + b²). */
public class PythagorasHypCalculator implements Calculator<PythagorasHypCalculator.Input> {

    public static final String FORMULA_ID = "trig.pythagoras-hyp";

    public record Input(double legA, double legB, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Pitágoras — hipotenusa",
            "Trigonometría",
            "c = √(a² + b²)",
            List.of(
                    InputDef.positive("legA", "Cateto a", "Ej. 3"),
                    InputDef.positive("legB", "Cateto b", "Ej. 4")),
            "Hipotenusa",
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
        double c = Guards.requireFiniteResult(Math.sqrt(a * a + b * b));
        List<String> steps = List.of(
                "Teorema de Pitagoras: c² = a² + b²",
                "Sustitucion: c = √(" + a + "² + " + b + "²)",
                "Resultado: c = " + c + " " + unit.getSymbol());
        return new CalculationResult(c, "Hipotenusa", "c = √(a² + b²)", steps,
                unit.getSymbol(), true, null);
    }
}
