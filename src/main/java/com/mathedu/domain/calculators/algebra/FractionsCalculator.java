package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.algebra.Fraction;
import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/**
 * Operaciones con fracciones exactas (simplificadas por MCD).
 * Operador: + - * / (tambien × ÷).
 */
public class FractionsCalculator implements Calculator<FractionsCalculator.Input> {

    public static final String FORMULA_ID = "arith.fractions";

    public record Input(long num1, long den1, String op, long num2, long den2) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Operaciones con fracciones",
            "Aritmética y álgebra",
            "a/b op c/d (resultado simplificado)",
            List.of(
                    new InputDef("num1", "Numerador 1", "Ej. 3", 0, 0, true),
                    new InputDef("den1", "Denominador 1", "Ej. 4", 0, 0, true),
                    InputDef.text("op", "Operador (+ - * /)", "Ej. +"),
                    new InputDef("num2", "Numerador 2", "Ej. 1", 0, 0, true),
                    new InputDef("den2", "Denominador 2", "Ej. 2", 0, 0, true)),
            "Resultado",
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
        if (in.den1() == 0 || in.den2() == 0) {
            throw new CalcException(ErrorCode.DIV_BY_ZERO,
                    in.den1() == 0 ? "den1" : "den2",
                    "El denominador no puede ser cero.");
        }
        String op = normalizeOp(in.op());
        Fraction f1 = Fraction.of(in.num1(), in.den1());
        Fraction f2 = Fraction.of(in.num2(), in.den2());
        final Fraction result;
        try {
            result = switch (op) {
                case "+" -> f1.add(f2);
                case "-" -> f1.subtract(f2);
                case "*" -> f1.multiply(f2);
                case "/" -> f1.divide(f2);
                default -> throw new CalcException(ErrorCode.INVALID_INPUT, "op",
                        "Operador '" + in.op() + "' no valido. Usa + - * /");
            };
        } catch (IllegalArgumentException e) {
            throw new CalcException(ErrorCode.DIV_BY_ZERO, "num2",
                    "Division entre cero (la segunda fraccion vale 0).");
        }
        double decimal = Guards.requireFiniteResult(result.doubleValue());
        List<String> steps = List.of(
                "Operacion: " + f1 + " " + op + " " + f2,
                "Resultado exacto (simplificado): " + result,
                "Valor decimal: " + decimal);
        return new CalculationResult(decimal, "Resultado",
                f1 + " " + op + " " + f2 + " = " + result, steps, "", true, null);
    }

    static String normalizeOp(String op) {
        if (op == null) {
            return "?";
        }
        return op.trim().replace("×", "*").replace("÷", "/");
    }
}
