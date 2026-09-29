package com.mathedu.infrastructure.symbolic;

import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.shared.errors.CalcException;

/** Defers Symja construction until the first symbolic operation is requested. */
public final class LazySymbolicEngine implements SymbolicEngine {

    private volatile SymbolicEngine delegate;

    private SymbolicEngine delegate() {
        SymbolicEngine current = delegate;
        if (current == null) {
            synchronized (this) {
                current = delegate;
                if (current == null) {
                    current = new SymjaSymbolicEngine();
                    delegate = current;
                }
            }
        }
        return current;
    }

    @Override public String simplify(String expression) throws CalcException {
        return delegate().simplify(expression);
    }

    @Override public String differentiate(String expression, String variable, int order)
            throws CalcException {
        return delegate().differentiate(expression, variable, order);
    }

    @Override public String integrate(String expression, String variable) throws CalcException {
        return delegate().integrate(expression, variable);
    }

    @Override public double integrateDefinite(String expression, String variable,
                                              double lower, double upper) throws CalcException {
        return delegate().integrateDefinite(expression, variable, lower, upper);
    }

    @Override public double limit(String expression, String variable, double point)
            throws CalcException {
        return delegate().limit(expression, variable, point);
    }
}