package com.mathedu.domain.calculators.calculus;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.domain.validation.Guards;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Differential and integral calculators backed by symbolic and numeric engines. */
public final class CalculusCalculator {

    private static final String DIFFERENTIAL = "Cálculo diferencial";
    private static final String INTEGRAL = "Cálculo integral";

    private CalculusCalculator() {
    }

    public record FunctionInput(String expression, String variable, double point, int order) { }
    public record FunctionPointInput(String expression, String variable, double point) { }
    public record DefiniteIntegralInput(String expression, String variable, double lower, double upper) { }

    public static final class Derivative implements Calculator<FunctionInput> {
        public static final String FORMULA_ID = "calculus.derivative";
        private static final FormulaDef DEF = new FormulaDef(FORMULA_ID, "Derivada simbólica",
                DIFFERENTIAL, "dⁿf/dxⁿ y evaluación en x₀", List.of(
                        InputDef.text("expression", "Función f(x)", "Ej. x^3 + sin(x)"),
                        InputDef.text("variable", "Variable", "x"),
                        new InputDef("point", "Punto de evaluación x₀", "Ej. 2", 0, 0, true),
                        new InputDef("order", "Orden de derivación", "Ej. 1", 1, 10, true)),
                "Derivada evaluada en x₀", "");
        private final SymbolicEngine symbolic;
        private final ExpressionEngine numeric;

        public Derivative(SymbolicEngine symbolic, ExpressionEngine numeric) {
            this.symbolic = Objects.requireNonNull(symbolic);
            this.numeric = Objects.requireNonNull(numeric);
        }

        @Override public FormulaDef formula() { return DEF; }

        @Override public CalculationResult calculate(FunctionInput in) throws CalcException {
            requireInput(in);
            requireVariable(in.variable());
            Guards.requireFinite(in.point(), "point", "Punto de evaluación");
            if (in.order() < 1 || in.order() > 10) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "order",
                        "El orden de derivación debe estar entre 1 y 10.");
            }
            String derivative = symbolic.differentiate(in.expression(), in.variable(), in.order());
            double value = numeric.evaluate(derivative, Map.of(in.variable(), in.point()));
            return new CalculationResult(value, "Derivada evaluada", "f^(" + in.order() + ")(x) = "
                    + derivative, List.of("Función: f(x) = " + in.expression(),
                    "Derivada simbólica: f^(" + in.order() + ")(x) = " + derivative,
                    "Evaluación: f^(" + in.order() + ")(" + in.point() + ") = " + value),
                    "", true, null);
        }
    }

    public static final class Antiderivative implements Calculator<FunctionPointInput> {
        public static final String FORMULA_ID = "calculus.antiderivative";
        private static final FormulaDef DEF = new FormulaDef(FORMULA_ID, "Integral indefinida",
                INTEGRAL, "F(x) = ∫f(x)dx y evaluación en x₀", List.of(
                        InputDef.text("expression", "Función f(x)", "Ej. x^2"),
                        InputDef.text("variable", "Variable", "x"),
                        new InputDef("point", "Punto de evaluación x₀", "Ej. 1", 0, 0, true)),
                "Antiderivada evaluada en x₀", "");
        private final SymbolicEngine symbolic;
        private final ExpressionEngine numeric;

        public Antiderivative(SymbolicEngine symbolic, ExpressionEngine numeric) {
            this.symbolic = Objects.requireNonNull(symbolic);
            this.numeric = Objects.requireNonNull(numeric);
        }

        @Override public FormulaDef formula() { return DEF; }

        @Override public CalculationResult calculate(FunctionPointInput in) throws CalcException {
            requireInput(in);
            String variable = requireVariable(in.variable());
            Guards.requireFinite(in.point(), "point", "Punto de evaluación");
            try {
            String antiderivative = symbolic.integrate(in.expression(), variable);
            double value = numeric.evaluate(antiderivative, Map.of(variable, in.point()));
            return new CalculationResult(value, "F(x₀)", "F(x) = " + antiderivative,
                List.of("Integrando: f(x) = " + in.expression(),
                    "Antiderivada: F(x) = " + antiderivative,
                    "Evaluación: F(" + in.point() + ") = " + value,
                    "La integral indefinida incluye una constante de integración C."),
                "", true, "La constante de integración C no afecta a la derivada.");
            } catch (CalcException symbolicFailure) {
            double value = integrateFromZero(numeric, in.expression(), variable, in.point());
            return new CalculationResult(value, "F(x₀) aproximada",
                "F(x₀) ≈ ∫₀ˣ⁰ f(t)dt", List.of(
                    "El cálculo simbólico no dio resultado: " + symbolicFailure.getMessage(),
                    "Se fija F(0)=0 y se integra numéricamente desde 0 hasta " + in.point() + ".",
                    "F(" + in.point() + ") ≈ " + value), "", false,
                "Antiderivada numérica aproximada con Simpson y constante de integración fijada en cero.");
            }
        }
    }

    public static final class DefiniteIntegral implements Calculator<DefiniteIntegralInput> {
        public static final String FORMULA_ID = "calculus.definite-integral";
        private static final FormulaDef DEF = new FormulaDef(FORMULA_ID, "Integral definida",
                INTEGRAL, "∫ₐᵇf(x)dx", List.of(
                        InputDef.text("expression", "Función f(x)", "Ej. x^2"),
                        InputDef.text("variable", "Variable", "x"),
                        new InputDef("lower", "Límite inferior a", "Ej. 0", 0, 0, true),
                        new InputDef("upper", "Límite superior b", "Ej. 1", 0, 0, true)),
                "Integral definida", "");
        private final SymbolicEngine symbolic;
        private final ExpressionEngine numeric;

        public DefiniteIntegral(SymbolicEngine symbolic, ExpressionEngine numeric) {
            this.symbolic = Objects.requireNonNull(symbolic);
            this.numeric = Objects.requireNonNull(numeric);
        }

        @Override public FormulaDef formula() { return DEF; }

        @Override public CalculationResult calculate(DefiniteIntegralInput in) throws CalcException {
            requireInput(in);
            String variable = requireVariable(in.variable());
            Guards.requireFinite(in.lower(), "lower", "Límite inferior");
            Guards.requireFinite(in.upper(), "upper", "Límite superior");
            try {
                double value = symbolic.integrateDefinite(in.expression(), variable,
                        in.lower(), in.upper());
                Guards.requireFiniteResult(value);
                return new CalculationResult(value, "Integral definida", "∫_" + in.lower() + "^"
                        + in.upper() + " " + in.expression() + " d" + variable,
                        List.of("Evaluación simbólica con Symja.",
                                "∫_" + in.lower() + "^" + in.upper() + " f(" + variable + ") d"
                                        + variable + " = " + value), "", true, null);
            } catch (CalcException symbolicFailure) {
                double value = integrateNumerically(in);
                return new CalculationResult(value, "Integral definida (aproximación)",
                        "Simpson: ∫_" + in.lower() + "^" + in.upper() + " " + in.expression()
                                + " d" + variable,
                        List.of("El cálculo simbólico no dio resultado: " + symbolicFailure.getMessage(),
                                "Se aplicó integración adaptativa Gauss-Legendre con respaldo Simpson.",
                                "Aproximación de la integral: " + value), "", false,
                        "Resultado numérico aproximado por Simpson.");
            }
        }

        private double integrateNumerically(DefiniteIntegralInput in) throws CalcException {
            if (in.lower() == in.upper()) {
                return 0;
            }
            boolean reverse = in.lower() > in.upper();
            double lower = reverse ? in.upper() : in.lower();
            double upper = reverse ? in.lower() : in.upper();
            UnivariateFunction function = x -> {
                try {
                    return numeric.evaluate(in.expression(), Map.of(in.variable(), x));
                } catch (CalcException e) {
                    throw new NumericEvaluationFailure(e);
                }
            };
            try {
                var integrator = new IterativeLegendreGaussIntegrator(
                        5, 1e-10, 1e-12, 3, 64);
                double value = integrator.integrate(100000, function, lower, upper);
                return Guards.requireFiniteResult(reverse ? -value : value);
            } catch (NumericEvaluationFailure e) {
                try {
                    throw e.calculationError;
                } catch (CalcException calculationError) {
                    throw calculationError;
                }
            } catch (RuntimeException gaussFailure) {
                try {
                    SimpsonIntegrator simpson = new SimpsonIntegrator();
                        double value = simpson.integrate(100000, function, lower, upper);
                        return Guards.requireFiniteResult(reverse ? -value : value);
                } catch (NumericEvaluationFailure e) {
                    throw e.calculationError;
                } catch (RuntimeException simpsonFailure) {
                    throw new CalcException(ErrorCode.DOMAIN_ERROR, "expression",
                            "Fallaron las aproximaciones numéricas de la integral.");
                }
            }
        }
    }

    private static double integrateFromZero(ExpressionEngine engine, String expression,
                                            String variable, double point) throws CalcException {
        if (point == 0) {
            return 0;
        }
        UnivariateFunction function = x -> {
            try {
                return engine.evaluate(expression, Map.of(variable, x));
            } catch (CalcException e) {
                throw new NumericEvaluationFailure(e);
            }
        };
        try {
            SimpsonIntegrator integrator = new SimpsonIntegrator(1e-10, 1e-12, 3, 64);
            double value = point > 0
                    ? integrator.integrate(100000, function, 0, point)
                    : -integrator.integrate(100000, function, point, 0);
            return Guards.requireFiniteResult(value);
        } catch (NumericEvaluationFailure e) {
            throw e.calculationError;
        } catch (RuntimeException e) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "expression",
                    "Falló la aproximación numérica de la antiderivada.");
        }
    }

    private static final class NumericEvaluationFailure extends RuntimeException {
        private final CalcException calculationError;
        private NumericEvaluationFailure(CalcException calculationError) {
            super(calculationError);
            this.calculationError = calculationError;
        }
    }

    private static void requireInput(Object input) throws CalcException {
        if (input == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacíos.");
        }
    }

    private static String requireVariable(String variable) throws CalcException {
        if (variable == null || !variable.matches("[A-Za-z][A-Za-z0-9_]*")) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "variable",
                    "La variable debe ser un identificador simple.");
        }
        return variable;
    }
}