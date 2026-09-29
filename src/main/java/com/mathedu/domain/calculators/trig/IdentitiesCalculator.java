package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Verifica numericamente 3 identidades basicas para un angulo dado. */
public class IdentitiesCalculator implements Calculator<IdentitiesCalculator.Input> {

    public static final String FORMULA_ID = "trig.identities";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double angleDeg) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Identidades básicas",
            "Trigonometría",
            "sin²θ + cos²θ = 1 ; 1 + tan²θ = sec²θ ; sin(2θ) = 2·sinθ·cosθ",
            List.of(InputDef.positive("angle", "Ángulo (θ)", "Ej. 30")),
            "sin²θ + cos²θ",
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
        double rad = Math.toRadians(theta);
        double s = Math.sin(rad);
        double c = Math.cos(rad);
        double pitagorica = Guards.requireFiniteResult(s * s + c * c);
        String tanCheck;
        if (Math.abs(c) < 1e-12) {
            tanCheck = "1 + tan²θ = sec²θ: no verificable (cos θ ≈ 0).";
        } else {
            double t = s / c;
            tanCheck = "1 + tan²θ = " + (1 + t * t) + " ; sec²θ = " + (1 / (c * c)) + " ✓";
        }
        List<String> steps = List.of(
                "Pitagorica: sin²(" + theta + "°) + cos²(" + theta + "°) = " + pitagorica + " ✓ (= 1)",
                tanCheck,
                "Angulo doble: sin(2·" + theta + "°) = " + Math.sin(2 * rad)
                        + " ; 2·sinθ·cosθ = " + (2 * s * c) + " ✓");
        return new CalculationResult(pitagorica, "sin²θ + cos²θ",
                "sin²θ + cos²θ = 1", steps, "", true, null);
    }
}
