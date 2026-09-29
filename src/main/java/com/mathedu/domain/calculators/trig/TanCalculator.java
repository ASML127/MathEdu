package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Tangente de un angulo; no definida en 90° + k·180°. */
public class TanCalculator implements Calculator<TanCalculator.Input> {

    public static final String FORMULA_ID = "trig.tan";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double angleDeg) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Tangente",
            "Trigonometría",
            "tan(θ) = sin(θ)/cos(θ)",
            List.of(InputDef.positive("angle", "Ángulo (θ)", "Ej. 45")),
            "tan(θ)",
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
        double mod = ((theta % 180) + 180) % 180;
        if (Math.abs(mod - 90) < 1e-9) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "angle",
                    "tan(" + theta + "°) no esta definida (asintota en 90° + k·180°).");
        }
        double t = Guards.requireFiniteResult(Math.tan(Math.toRadians(theta)));
        List<String> steps = List.of(
                "Funcion: tan(θ) = sin(θ)/cos(θ) con θ = " + theta + "°",
                "Resultado: tan(" + theta + "°) = " + t);
        return new CalculationResult(t, "tan(θ)", "tan(θ) = sin(θ)/cos(θ)",
                steps, "", true, null);
    }
}
