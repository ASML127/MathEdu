package com.mathedu.infrastructure.engine;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Adaptador EvalEx 3.x del motor de expresiones.
 *
 * <p>Normalizacion amigable: coma decimal, simbolos × ÷, constantes PI/E,
 * funciones SIN/COS/TAN en grados (SINR/COSR/TANR en radianes),
 * SQRT, LOG (natural), LOG10, ABS, ^ para potencias.
 * La coma como separador de argumentos no se soporta (usar punto).
 */
public class EvalExEngine implements ExpressionEngine {

    @Override
    public double evaluate(String expression, Map<String, Double> variables) throws CalcException {
        String normalized = normalize(expression);
        try {
            Expression exp = new Expression(normalized);
            if (variables != null) {
                variables.forEach((k, v) -> exp.with(k, BigDecimal.valueOf(v)));
            }
            EvaluationValue result = exp.evaluate();
            if (!result.isNumberValue()) {
                throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                        "La expresion debe devolver un numero.");
            }
            return Guards.requireFiniteResult(result.getNumberValue().doubleValue());
        } catch (ParseException e) {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "Expresion malformada: " + cleanMessage(e.getMessage()));
        } catch (EvaluationException e) {
            throw mapEvaluationError(e);
        } catch (ArithmeticException e) {
            throw new CalcException(ErrorCode.OVERFLOW, "expr",
                    "Desbordamiento al evaluar la expresion.");
        }
    }

    @Override
    public Set<String> usedVariables(String expression) throws CalcException {
        String normalized = normalize(expression);
        try {
            return new Expression(normalized).getUsedVariables();
        } catch (ParseException e) {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "Expresion malformada: " + cleanMessage(e.getMessage()));
        }
    }

    private static String normalize(String expression) throws CalcException {
        if (expression == null || expression.isBlank()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "expr",
                    "Introduce una expresion. Ej.: 2*x^2 + SIN(30)");
        }
        String text = expression.trim()
                .replace('×', '*')
                .replace('÷', '/')
                // coma decimal solo entre digitos (no rompe separadores)
                .replaceAll("(?<=\\d),(?=\\d)", ".");
        if (text.length() > MAX_LENGTH) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "expr",
                    "Expresion demasiado larga (max " + MAX_LENGTH + " caracteres).");
        }
        return text;
    }

    private static CalcException mapEvaluationError(EvaluationException e) {
        String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (msg.contains("zero") || msg.contains("cero") || msg.contains("division")) {
            return new CalcException(ErrorCode.DIV_BY_ZERO, "expr",
                    "Division entre cero al evaluar la expresion.");
        }
        if (msg.contains("undefined variable") || msg.contains("unknown")) {
            return new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "Variable o funcion desconocida: " + cleanMessage(e.getMessage()));
        }
        return new CalcException(ErrorCode.DOMAIN_ERROR, "expr",
                "No se pudo evaluar: " + cleanMessage(e.getMessage()));
    }

    private static String cleanMessage(String msg) {
        if (msg == null) {
            return "";
        }
        int nl = msg.indexOf('\n');
        String first = (nl < 0 ? msg : msg.substring(0, nl)).trim();
        return first.length() > 160 ? first.substring(0, 160) + "…" : first;
    }
}
