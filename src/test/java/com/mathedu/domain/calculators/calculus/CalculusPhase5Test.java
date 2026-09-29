package com.mathedu.domain.calculators.calculus;

import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.infrastructure.engine.EvalExEngine;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculusPhase5Test {

    private final ExpressionEngine numeric = new EvalExEngine();

    @Test
    void computesDerivativeAndEvaluatesAtPoint() throws CalcException {
        var calculator = new CalculusCalculator.Derivative(new FakeSymbolicEngine(), numeric);
        var result = calculator.calculate(new CalculusCalculator.FunctionInput("x^2", "x", 3, 1));

        assertThat(result.value()).isEqualTo(6);
        assertThat(result.formula()).isEqualTo("f^(1)(x) = 2*x");
        assertThat(result.steps()).anyMatch(step -> step.contains("f^(1)(3.0) = 6.0"));
    }

    @Test
    void evaluatesIndefiniteIntegralAtPoint() throws CalcException {
        var calculator = new CalculusCalculator.Antiderivative(new FakeSymbolicEngine(), numeric);
        var result = calculator.calculate(
                new CalculusCalculator.FunctionPointInput("x^2", "x", 3));

        assertThat(result.value()).isEqualTo(9);
        assertThat(result.warning()).contains("constante de integración");
    }

        @Test
        void indefiniteIntegralFallsBackToApproximation() throws CalcException {
        var calculator = new CalculusCalculator.Antiderivative(
            new FailingAntiderivativeEngine(), numeric);
        var result = calculator.calculate(
            new CalculusCalculator.FunctionPointInput("x^2", "x", 3));

        assertThat(result.value()).isCloseTo(9,
            org.assertj.core.data.Offset.offset(1e-10));
        assertThat(result.exact()).isFalse();
        assertThat(result.warning()).contains("Simpson");
        }

    @Test
    void definiteIntegralUsesSymbolicResultAndFallsBackToNumerical() throws CalcException {
        var input = new CalculusCalculator.DefiniteIntegralInput("x^2", "x", 0, 1);
        var symbolic = new CalculusCalculator.DefiniteIntegral(new FakeSymbolicEngine(), numeric)
                .calculate(input);
        assertThat(symbolic.value()).isCloseTo(1.0 / 3,
                org.assertj.core.data.Offset.offset(1e-12));
        assertThat(symbolic.exact()).isTrue();

        var fallback = new CalculusCalculator.DefiniteIntegral(
                new FailingIntegralEngine(), numeric).calculate(input);
        assertThat(fallback.value()).isCloseTo(1.0 / 3,
                org.assertj.core.data.Offset.offset(1e-9));
        assertThat(fallback.exact()).isFalse();
        assertThat(fallback.warning()).contains("Simpson");

        var reversed = new CalculusCalculator.DefiniteIntegral(
            new FailingIntegralEngine(), numeric).calculate(
            new CalculusCalculator.DefiniteIntegralInput("x^2", "x", 1, 0));
        assertThat(reversed.value()).isCloseTo(-1.0 / 3,
            org.assertj.core.data.Offset.offset(1e-9));
    }

    @Test
    void derivativeOrderIsValidated() {
        var calculator = new CalculusCalculator.Derivative(new FakeSymbolicEngine(), numeric);
        assertThatThrownBy(() -> calculator.calculate(
                new CalculusCalculator.FunctionInput("x^2", "x", 3, 11)))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
    }

    private static class FakeSymbolicEngine implements SymbolicEngine {
        @Override public String simplify(String expression) { return expression; }
        @Override public String differentiate(String expression, String variable, int order) {
            return order == 1 ? "2*x" : "2";
        }
        @Override public String integrate(String expression, String variable) throws CalcException {
            return "x^3/3";
        }
        @Override public double integrateDefinite(String expression, String variable,
                              double lower, double upper) throws CalcException {
            return 1.0 / 3;
        }
        @Override public double limit(String expression, String variable, double point) { return 2; }
    }

    private static final class FailingIntegralEngine extends FakeSymbolicEngine {
        @Override public double integrateDefinite(String expression, String variable,
                                                  double lower, double upper) throws CalcException {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr", "Forma no reconocida");
        }
    }

    private static final class FailingAntiderivativeEngine extends FakeSymbolicEngine {
        @Override public String integrate(String expression, String variable) throws CalcException {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr", "No existe antiderivada cerrada");
        }
    }
}