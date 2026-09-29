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

/** Teorema de Pitagoras: cateto b = √(c² - a²). */
public class PythagorasLegCalculator implements Calculator<PythagorasLegCalculator.Input> {

    public static final String FORMULA_ID = "trig.pythagoras-leg";

    public record Input(double hyp, double legA, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Pitágoras — cateto",
            "Trigonometría",
            "b = √(c² - a²)",
            List.of(
                    InputDef.positive("hyp", "Hipotenusa (c)", "Ej. 5"),
                    InputDef.positive("legA", "Cateto conocido (a)", "Ej. 3")),
            "Cateto",
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
        double c = Guards.requirePositive(in.hyp(), "hyp", "Hipotenusa");
        double a = Guards.requirePositive(in.legA(), "legA", "Cateto conocido");
        if (a >= c) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "legA",
                    "El cateto (" + a + ") debe ser menor que la hipotenusa (" + c + ").");
        }
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double b = Guards.requireFiniteResult(Math.sqrt(c * c - a * a));
        List<String> steps = List.of(
                "Despeje: b² = c² - a²",
                "Sustitucion: b = √(" + c + "² - " + a + "²)",
                "Resultado: b = " + b + " " + unit.getSymbol());
        return new CalculationResult(b, "Cateto", "b = √(c² - a²)", steps,
                unit.getSymbol(), true, null);
    }
}
