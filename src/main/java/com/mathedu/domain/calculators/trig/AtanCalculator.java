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

/** Arcotangente: devuelve el angulo en la unidad seleccionada (°/rad). */
public class AtanCalculator implements Calculator<AtanCalculator.Input> {

    public static final String FORMULA_ID = "trig.atan";

    public record Input(double value, AngleUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Arcotangente",
            "Trigonometría",
            "θ = arctan(x)",
            List.of(new InputDef("x", "Valor (x)", "Ej. 1", 0, 0, true)),
            "θ = arctan(x)",
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
        double x = Guards.requireFinite(in.value(), "x", "Valor");
        AngleUnit au = in.unit() == null ? AngleUnit.DEG : in.unit();
        double deg = Math.toDegrees(Math.atan(x));
        double out = Guards.requireFiniteResult(au.fromDegrees(deg));
        List<String> steps = List.of(
                "Funcion inversa: θ = arctan(" + x + ")",
                "En grados: θ = " + deg + "°",
                "Resultado: θ = " + out + " " + au.getSymbol());
        return new CalculationResult(out, "θ = arctan(x)", "θ = arctan(x)",
                steps, au.getSymbol(), true, null);
    }
}
