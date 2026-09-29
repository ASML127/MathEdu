package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Potencia: p = base^exp. */
public class PowerCalculator implements Calculator<PowerCalculator.Input> {

    public static final String FORMULA_ID = "arith.power";

    public record Input(double base, double exponent) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Potencia",
            "Aritmética y álgebra",
            "p = base^exp",
            List.of(
                    new InputDef("base", "Base", "Ej. 2", 0, 0, true),
                    new InputDef("exp", "Exponente", "Ej. 10", 0, 0, true)),
            "Potencia",
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
        double base = Guards.requireFinite(in.base(), "base", "Base");
        double exp = Guards.requireFinite(in.exponent(), "exp", "Exponente");
        if (base == 0 && exp == 0) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "base",
                    "0^0 es una forma indeterminada.");
        }
        if (base == 0 && exp < 0) {
            throw new CalcException(ErrorCode.DIV_BY_ZERO, "exp",
                    "0 elevado a exponente negativo equivale a 1/0.");
        }
        double p = Math.pow(base, exp);
        if (Double.isNaN(p)) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "base",
                    "Resultado no real (p. ej. base negativa con exponente fraccionario).");
        }
        p = Guards.requireFiniteResult(p);
        List<String> steps = List.of(
                "Formula: p = base^exp",
                "Sustitucion: p = (" + base + ")^(" + exp + ")",
                "Resultado: p = " + p);
        return new CalculationResult(p, "Potencia", "p = base^exp", steps, "", true, null);
    }
}
