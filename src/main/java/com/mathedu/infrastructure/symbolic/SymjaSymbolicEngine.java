package com.mathedu.infrastructure.symbolic;

import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Symja adapter with a small expression whitelist and a hard caller timeout.
 *
 * <p>Two details worth knowing:
 * <ul>
 *   <li>Creating the first {@link ExprEvaluator} loads hundreds of Symja classes
 *       and costs about two seconds. That warm-up happens once per JVM, outside
 *       the timeout budget, so the first user-visible operation is not killed
 *       by the deadline meant to protect against runaway simplification.</li>
 *   <li>{@code N(expr, 20)} keeps twenty significant digits, and the
 *       {@code 3.33*10^-1} notation Symja uses for those digits is parsed back
 *       explicitly. Bounds are wrapped in {@code Rationalize} so the
 *       computation itself does not collapse to machine precision.</li>
 * </ul>
 */
public final class SymjaSymbolicEngine implements SymbolicEngine {

    public static final int MAX_EXPRESSION_LENGTH = 200;
    public static final long TIMEOUT_SECONDS = 3;
    /** Significant digits requested from Symja for numeric results. */
    private static final int NUMERIC_PRECISION = 20;
    private static final Pattern IDENTIFIER = Pattern.compile("[A-Za-z_][A-Za-z_0-9]*");
    /** Symja prints small/large results as {@code 3.33*10^-1}. */
    private static final Pattern SCIENTIFIC = Pattern.compile(
            "^([+-]?[0-9.]+)\\*10\\^([+-]?[0-9]+)$");
    private static final Set<String> FUNCTIONS = Set.of(
            "sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh",
            "sqrt", "abs", "exp", "log", "ln");

    @Override
    public String simplify(String expression) throws CalcException {
        String normalized = normalize(expression, "x");
        return run(() -> evaluate("Simplify(" + normalized + ")").toString());
    }

    @Override
    public String differentiate(String expression, String variable, int order) throws CalcException {
        String symbol = normalizeVariable(variable);
        String normalized = normalize(expression, symbol);
        if (order < 1 || order > 10) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "order",
                    "El orden de derivación debe estar entre 1 y 10.");
        }
        return run(() -> {
            String current = normalized;
            for (int i = 0; i < order; i++) {
                current = evaluate("D(" + current + "," + symbol + ")").toString();
            }
            return evaluate("Simplify(" + current + ")").toString();
        });
    }

    @Override
    public String integrate(String expression, String variable) throws CalcException {
        String symbol = normalizeVariable(variable);
        String normalized = normalize(expression, symbol);
        return run(() -> evaluate("Integrate(" + normalized + "," + symbol + ")").toString());
    }

    @Override
    public double integrateDefinite(String expression, String variable, double lower, double upper)
            throws CalcException {
        String symbol = normalizeVariable(variable);
        String normalized = normalize(expression, symbol);
        requireFiniteBound(lower, "lower");
        requireFiniteBound(upper, "upper");
        return run(() -> numeric("N(Integrate(" + normalized + ",{" + symbol + ","
                + exact(lower) + "," + exact(upper) + "})," + NUMERIC_PRECISION + ")"));
    }

    @Override
    public double limit(String expression, String variable, double point) throws CalcException {
        String symbol = normalizeVariable(variable);
        String normalized = normalize(expression, symbol);
        requireFiniteBound(point, "point");
        return run(() -> numeric("N(Limit(" + normalized + "," + symbol + "->" + exact(point)
                + ")," + NUMERIC_PRECISION + ")"));
    }

    /**
     * Wraps a bound so Symja works in exact arithmetic.
     *
     * <p>Passing {@code 0.0} or {@code 0.5} directly makes Symja integrate in
     * machine arithmetic, and the answer is then already rounded to six
     * significant digits no matter how much precision {@code N} is asked for.
     * {@code Rationalize} turns the bound into the exact fraction the user
     * typed, so the whole computation stays exact until the final {@code N}.
     */
    private static String exact(double value) {
        return "Rationalize(" + value + ")";
    }

    private static IExpr evaluate(String expression) {
        return new ExprEvaluator().eval(expression);
    }

    /** One-time, JVM-wide warm-up of Symja's class loading. */
    private static final class WarmUp {

        private static final boolean READY = warmUp();

        private WarmUp() {
        }

        private static boolean warmUp() {
            new ExprEvaluator().eval("1+1");
            return true;
        }

        static void ensure() {
            // Reading READY triggers the class initialiser, which pays Symja's
            // class-loading cost once for the whole JVM, outside any deadline.
            if (!READY) {
                throw new IllegalStateException("warm-up no completado");
            }
        }
    }

    private static double numeric(String expression) throws CalcException {
        return toDouble(evaluate(expression).toString());
    }

    /** Parses Symja's output, including its {@code mantissa*10^exponent} form. */
    static double toDouble(String raw) throws CalcException {
        String text = raw.trim().replace(" ", "");
        double value;
        Matcher scientific = SCIENTIFIC.matcher(text);
        try {
            if (scientific.matches()) {
                value = Double.parseDouble(scientific.group(1))
                        * Math.pow(10, Integer.parseInt(scientific.group(2)));
            } else {
                value = Double.parseDouble(text);
            }
        } catch (NumberFormatException | ArithmeticException e) {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "El cálculo simbólico no dio un resultado numérico real (" + raw.trim()
                            + "). Revisa la expresión o el dominio del punto.");
        }
        if (!Double.isFinite(value)) {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "El cálculo simbólico dio un resultado no finito (" + raw.trim() + ").");
        }
        return value;
    }

    private static String normalize(String expression, String variable) throws CalcException {
        if (expression == null || expression.isBlank()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "expr", "Introduce una expresión.");
        }
        String text = expression.trim().replace('×', '*').replace('÷', '/');
        if (text.length() > MAX_EXPRESSION_LENGTH) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "expr",
                    "La expresión supera " + MAX_EXPRESSION_LENGTH + " caracteres.");
        }
        if (!text.matches("[A-Za-z0-9_.,+\\-*/^()\\s]+")) {
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "La expresión contiene caracteres no permitidos.");
        }
        Matcher matcher = IDENTIFIER.matcher(text);
        while (matcher.find()) {
            String identifier = matcher.group().toLowerCase(Locale.ROOT);
            if (!identifier.equals(variable.toLowerCase(Locale.ROOT))
                    && !identifier.equals("pi") && !identifier.equals("e")
                    && !FUNCTIONS.contains(identifier)) {
                throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                        "Símbolo no permitido: " + matcher.group());
            }
        }
        return text.replaceAll("(?i)\\bsin\\b", "Sin")
                .replaceAll("(?i)\\bcos\\b", "Cos")
                .replaceAll("(?i)\\btan\\b", "Tan")
                .replaceAll("(?i)\\basin\\b", "ArcSin")
                .replaceAll("(?i)\\bacos\\b", "ArcCos")
                .replaceAll("(?i)\\batan\\b", "ArcTan")
                .replaceAll("(?i)\\bsinh\\b", "Sinh")
                .replaceAll("(?i)\\bcosh\\b", "Cosh")
                .replaceAll("(?i)\\btanh\\b", "Tanh")
                .replaceAll("(?i)\\bsqrt\\b", "Sqrt")
                .replaceAll("(?i)\\babs\\b", "Abs")
                .replaceAll("(?i)\\bexp\\b", "Exp")
                .replaceAll("(?i)\\bln\\b", "Log")
                .replaceAll("(?i)\\blog\\b", "Log")
                .replaceAll("(?i)\\bpi\\b", "Pi");
    }

    private static String normalizeVariable(String variable) throws CalcException {
        if (variable == null || !variable.matches("[A-Za-z][A-Za-z0-9_]*")) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "variable",
                    "La variable debe ser un identificador simple.");
        }
        return variable;
    }

    private static void requireFiniteBound(double value, String field) throws CalcException {
        if (!Double.isFinite(value)) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field, "El límite debe ser finito.");
        }
    }

    private static <T> T run(Callable<T> action) throws CalcException {
        WarmUp.ensure();
        FutureTask<T> task = new FutureTask<>(action);
        Thread worker = new Thread(task, "symja-operation");
        worker.setDaemon(true);
        worker.start();
        try {
            return task.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            task.cancel(true);
            throw new CalcException(ErrorCode.TIMEOUT, "expr",
                    "El cálculo simbólico superó el límite de " + TIMEOUT_SECONDS + " segundos.");
        } catch (InterruptedException e) {
            task.cancel(true);
            Thread.currentThread().interrupt();
            throw new CalcException(ErrorCode.TIMEOUT, "expr", "El cálculo simbólico fue cancelado.");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CalcException ce) {
                throw ce;
            }
            throw new CalcException(ErrorCode.BAD_EXPRESSION, "expr",
                    "Symja no pudo procesar la expresión: "
                            + (cause == null ? "error desconocido" : cause.getMessage()));
        }
    }
}