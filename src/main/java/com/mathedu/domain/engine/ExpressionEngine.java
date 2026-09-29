package com.mathedu.domain.engine;

import com.mathedu.shared.errors.CalcException;

import java.util.Map;
import java.util.Set;

/**
 * Puerto del motor de expresiones (evaluacion numerica).
 * La implementacion EvalEx vive en {@code infrastructure.engine};
 * el calculo simbolico llegara en Fase 5 tras esta misma interfaz.
 */
public interface ExpressionEngine {

    /** Longitud maxima de expresion aceptada (robustez). */
    int MAX_LENGTH = 200;

    /**
     * Evalua la expresion con las variables dadas.
     *
     * @param expression texto como "2*x^2 + SIN(30)" (acepta coma decimal, × ÷)
     * @param variables  valores por nombre (p. ej. x)
     * @return resultado numerico finito
     * @throws CalcException BAD_EXPRESSION, DIV_BY_ZERO, DOMAIN_ERROR u OVERFLOW
     */
    double evaluate(String expression, Map<String, Double> variables) throws CalcException;

    /** Nombres de variables usadas en la expresion (p. ej. [x]). */
    Set<String> usedVariables(String expression) throws CalcException;
}
