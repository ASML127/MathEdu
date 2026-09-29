package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Evaluacion de expresiones libres con el motor (EvalEx).
 * Acepta una variable 'x' opcional; cualquier otra variable se rechaza.
 */
public class ExpressionCalculator implements Calculator<ExpressionCalculator.Input> {

    public static final String FORMULA_ID = "arith.expr";

    public record Input(String expression, double x) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Evaluar expresión",
            "Aritmética y álgebra",
            "Expresion con + - * / ^ ( ) y funciones",
            List.of(
                    InputDef.text("expr", "Expresión", "Ej. 2*x^2 + SIN(30)"),
                    InputDef.optional("x", "Valor de x (0 si no hay variable)", "Ej. 3")),
            "Valor",
            "");

    private final ExpressionEngine engine;

    public ExpressionCalculator(ExpressionEngine engine) {
        this.engine = Objects.requireNonNull(engine, "engine");
    }

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null || in.expression() == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "expr",
                    "Introduce una expresion.");
        }
        Set<String> vars = engine.usedVariables(in.expression());
        for (String v : vars) {
            if (!v.equals("x") && !v.equals("X")) {
                throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                        "Variable desconocida '" + v + "'. Solo se permite 'x'.");
            }
        }
        double value = engine.evaluate(in.expression(), Map.of("x", in.x(), "X", in.x()));
        List<String> steps = List.of(
                "Expresion: " + in.expression().trim(),
                vars.isEmpty()
                        ? "Sin variables: evaluacion directa."
                        : "Sustitucion: x = " + in.x(),
                "Resultado: " + value);
        return new CalculationResult(value, "Valor", in.expression().trim(), steps, "",
                false, "Evaluacion numerica (no simbolica).");
    }
}
