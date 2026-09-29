package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.AngleUnit;

import java.util.List;

/**
 * Conversion grados ⇄ radianes. La unidad seleccionada es el origen;
 * el resultado sale en la otra unidad.
 */
public class AngleConvertCalculator implements Calculator<AngleConvertCalculator.Input> {

    public static final String FORMULA_ID = "trig.convert";

    public record Input(double value, AngleUnit from) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Convertir grados ⇄ radianes",
            "Trigonometría",
            "rad = °·π/180 ; ° = rad·180/π",
            List.of(new InputDef("value", "Valor", "Ej. 180", 0, 0, true)),
            "Valor convertido",
            "°/rad");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double v = Guards.requireFinite(in.value(), "value", "Valor");
        AngleUnit from = in.from() == null ? AngleUnit.DEG : in.from();
        final double out;
        final String formula;
        final AngleUnit to;
        final List<String> steps;
        if (from == AngleUnit.DEG) {
            to = AngleUnit.RAD;
            out = Guards.requireFiniteResult(Math.toRadians(v));
            formula = "rad = ° · π / 180";
            steps = List.of(
                    "Formula: radianes = grados · π / 180",
                    "Sustitucion: " + v + "° · π / 180",
                    "Resultado: " + v + "° = " + out + " rad");
        } else {
            to = AngleUnit.DEG;
            out = Guards.requireFiniteResult(Math.toDegrees(v));
            formula = "° = rad · 180 / π";
            steps = List.of(
                    "Formula: grados = radianes · 180 / π",
                    "Sustitucion: " + v + " rad · 180 / π",
                    "Resultado: " + v + " rad = " + out + "°");
        }
        return new CalculationResult(out, "Valor convertido", formula, steps,
                to.getSymbol(), true, null);
    }
}
