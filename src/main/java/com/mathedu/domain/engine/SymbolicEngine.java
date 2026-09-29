package com.mathedu.domain.engine;

import com.mathedu.shared.errors.CalcException;

/** Port for bounded symbolic operations; implementations belong to infrastructure. */
public interface SymbolicEngine {

    String simplify(String expression) throws CalcException;

    String differentiate(String expression, String variable, int order) throws CalcException;

    String integrate(String expression, String variable) throws CalcException;

    double integrateDefinite(String expression, String variable, double lower, double upper)
            throws CalcException;

    double limit(String expression, String variable, double point) throws CalcException;
}