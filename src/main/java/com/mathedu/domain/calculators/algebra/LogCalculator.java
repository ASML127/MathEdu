package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Logaritmo en base b de x: L = log_b(x). */
public class LogCalculator implements Calculator<LogCalculator.Input> {

    public static final String FORMULA_ID = "arith.log";

    public record Input(double value, double base) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Logaritmo",
            "Aritmética y álgebra",
            "L = log_b(x) = ln(x)/ln(b)",
            List.of(
                    new InputDef("x", "Argumento (x)", "Ej. 100", 0, 0, true),
                    new InputDef("base", "Base (b)", "Ej. 10 (o e≈2.718)", 0, 0, true)),
            "Logaritmo",
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
        double x = Guards.requireFinite(in.value(), "x", "Argumento");
        double b = Guards.requireFinite(in.base(), "base", "Base");
        if (x <= 0) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "x",
                    "El logaritmo solo esta definido para x > 0. Recibido: " + x);
        }
        if (b <= 0 || b == 1) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "base",
                    "La base debe ser > 0 y distinta de 1. Recibida: " + b);
        }
        double l = Guards.requireFiniteResult(Math.log(x) / Math.log(b));
        List<String> steps = List.of(
                "Formula: L = ln(x)/ln(b)",
                "Sustitucion: L = ln(" + x + ")/ln(" + b + ")",
                "Resultado: L = " + l);
        return new CalculationResult(l, "Logaritmo", "L = log_b(x)", steps, "", true, null);
    }
}
