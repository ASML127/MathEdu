package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Seno de un angulo (grados internos; respeta el selector °/rad). */
public class SinCalculator implements Calculator<SinCalculator.Input> {

    public static final String FORMULA_ID = "trig.sin";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double angleDeg) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Seno",
            "Trigonometría",
            "sin(θ)",
            List.of(InputDef.positive("angle", "Ángulo (θ)", "Ej. 30")),
            "sin(θ)",
            "");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double theta = Guards.requireFinite(in.angleDeg(), "angle", "Angulo");
        double s = Guards.requireFiniteResult(Math.sin(Math.toRadians(theta)));
        List<String> steps = List.of(
                "Funcion: sin(θ) con θ = " + theta + "° = " + Math.toRadians(theta) + " rad",
                "Resultado: sin(" + theta + "°) = " + s);
        return new CalculationResult(s, "sin(θ)", "sin(θ)", steps, "", true, null);
    }
}
