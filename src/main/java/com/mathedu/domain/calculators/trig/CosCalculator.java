package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Coseno de un angulo (grados internos; respeta el selector °/rad). */
public class CosCalculator implements Calculator<CosCalculator.Input> {

    public static final String FORMULA_ID = "trig.cos";

    /** Angulo siempre en grados (la fabrica convierte desde radianes). */
    public record Input(double angleDeg) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Coseno",
            "Trigonometría",
            "cos(θ)",
            List.of(InputDef.positive("angle", "Ángulo (θ)", "Ej. 60")),
            "cos(θ)",
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
        double c = Guards.requireFiniteResult(Math.cos(Math.toRadians(theta)));
        List<String> steps = List.of(
                "Funcion: cos(θ) con θ = " + theta + "° = " + Math.toRadians(theta) + " rad",
                "Resultado: cos(" + theta + "°) = " + c);
        return new CalculationResult(c, "cos(θ)", "cos(θ)", steps, "", true, null);
    }
}
