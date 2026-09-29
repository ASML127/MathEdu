package com.mathedu.domain.calculators.series;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.domain.validation.Guards;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Sequences, partial sums and elementary convergence criteria. */
public final class SeriesCalculator {

    private static final String CATEGORY = "Series y sucesiones";
    private SeriesCalculator() { }

    public record ArithmeticInput(double first, double difference, int terms) { }
    public record GeometricInput(double first, double ratio, int terms) { }
    public record InfiniteGeometricInput(double first, double ratio) { }
    public record FiniteSumInput(String expression, int start, int end) { }
    public record CriterionInput(double limit) { }
    public record ComparisonInput(double limit, String mode) { }
    public record LimitInput(String expression, String variable, double point) { }

    public static final class ArithmeticSequence implements Calculator<ArithmeticInput> {
        public static final String FORMULA_ID = "series.arithmetic-sequence";
        private static final FormulaDef DEF = def(FORMULA_ID, "Sucesión aritmética",
                "aₙ = a₁+(n-1)d; Sₙ = n(a₁+aₙ)/2", List.of(
                        number("first", "Primer término a₁"), number("difference", "Diferencia d"),
                        integer("terms", "Términos n")), "Término aₙ");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(ArithmeticInput in) throws CalcException {
            requireInput(in);
            requireTerms(in.terms());
            double last = Guards.requireFiniteResult(in.first() + (in.terms() - 1.0) * in.difference());
            double sum = Guards.requireFiniteResult(in.terms() * (in.first() + last) / 2.0);
            return result(last, "Término aₙ", "aₙ = a₁+(n-1)d", List.of(
                    "aₙ = " + in.first() + "+(" + in.terms() + "-1)·" + in.difference()
                            + " = " + last,
                    "Sₙ = n(a₁+aₙ)/2 = " + sum));
        }
    }

    public static final class GeometricSequence implements Calculator<GeometricInput> {
        public static final String FORMULA_ID = "series.geometric-sequence";
        private static final FormulaDef DEF = def(FORMULA_ID, "Sucesión geométrica",
                "aₙ = a₁r^(n-1); Sₙ = a₁(rⁿ-1)/(r-1)", List.of(
                        number("first", "Primer término a₁"), number("ratio", "Razón r"),
                        integer("terms", "Términos n")), "Término aₙ");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(GeometricInput in) throws CalcException {
            requireInput(in);
            requireTerms(in.terms());
            double last = Guards.requireFiniteResult(in.first() * Math.pow(in.ratio(), in.terms() - 1));
            double sum = in.ratio() == 1 ? in.first() * in.terms()
                    : in.first() * (Math.pow(in.ratio(), in.terms()) - 1) / (in.ratio() - 1);
            sum = Guards.requireFiniteResult(sum);
            return result(last, "Término aₙ", "aₙ = a₁r^(n-1)", List.of(
                    "aₙ = " + in.first() + "·" + in.ratio() + "^" + (in.terms() - 1) + " = " + last,
                    "Sₙ = " + sum));
        }
    }

    public static final class InfiniteGeometric implements Calculator<InfiniteGeometricInput> {
        public static final String FORMULA_ID = "series.infinite-geometric";
        private static final FormulaDef DEF = def(FORMULA_ID, "Serie geométrica infinita",
                "S = a₁/(1-r), si |r|<1", List.of(
                        number("first", "Primer término a₁"), number("ratio", "Razón r")), "Suma infinita");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(InfiniteGeometricInput in) throws CalcException {
            requireInput(in);
            Guards.requireFinite(in.first(), "first", "Primer término");
            double ratio = Guards.requireFinite(in.ratio(), "ratio", "Razón");
            if (Math.abs(ratio) >= 1) {
                throw new CalcException(ErrorCode.DOMAIN_ERROR, "ratio",
                        "La serie geométrica diverge porque |r| debe ser menor que 1.");
            }
            double sum = Guards.requireFiniteResult(in.first() / (1 - ratio));
            return result(sum, "Suma infinita", "S = a₁/(1-r)", List.of(
                    "|r| = " + Math.abs(ratio) + " < 1: la serie converge.",
                    "S = " + in.first() + "/(1 - " + ratio + ") = " + sum));
        }
    }

    public static final class FiniteSum implements Calculator<FiniteSumInput> {
        public static final String FORMULA_ID = "series.finite-sum";
        private static final FormulaDef DEF = def(FORMULA_ID, "Suma parcial de expresión",
                "Σ f(n), n = inicio…fin", List.of(
                        InputDef.text("expression", "Término f(n)", "Ej. 1/n^2"),
                        integer("start", "Inicio n₀"), integer("end", "Fin N")), "Suma parcial");
        private final ExpressionEngine engine;
        public FiniteSum(ExpressionEngine engine) { this.engine = Objects.requireNonNull(engine); }
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(FiniteSumInput in) throws CalcException {
            requireInput(in);
            long count = (long) in.end() - in.start() + 1;
            if (count < 1 || count > 100000) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "end",
                        "El intervalo debe contener entre 1 y 100000 términos.");
            }
            double sum = 0;
            for (long n = in.start(); n <= in.end(); n++) {
                sum = Guards.requireFiniteResult(sum + engine.evaluate(in.expression(), Map.of("n", (double) n)));
            }
            return result(sum, "Suma parcial", "Σ[n=" + in.start() + ".." + in.end() + "] "
                    + in.expression(), List.of("Cantidad de términos: " + count,
                    "Suma parcial: S = " + sum));
        }
    }

    public static final class RatioTest implements Calculator<CriterionInput> {
        public static final String FORMULA_ID = "series.ratio-test";
        private static final FormulaDef DEF = criterionDef(FORMULA_ID, "Criterio de la razón");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(CriterionInput in) throws CalcException {
            return criterion(in, "L = lim |aₙ₊₁/aₙ|", "de la razón");
        }
    }

    public static final class RootTest implements Calculator<CriterionInput> {
        public static final String FORMULA_ID = "series.root-test";
        private static final FormulaDef DEF = criterionDef(FORMULA_ID, "Criterio de la raíz");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(CriterionInput in) throws CalcException {
            return criterion(in, "L = lim ⁿ√|aₙ|", "de la raíz");
        }
    }

        public static final class LimitComparison implements Calculator<ComparisonInput> {
        public static final String FORMULA_ID = "series.limit-comparison";
        private static final FormulaDef DEF = def(FORMULA_ID, "Comparación por límite",
            "Si lim(aₙ/bₙ)=c, 0<c<∞, ambas series tienen el mismo carácter", List.of(
                number("limit", "Límite de aₙ/bₙ"),
                InputDef.text("mode", "Modo de comparación", "mayorante-convergente")),
            "Conclusión");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(ComparisonInput in) throws CalcException {
            requireInput(in);
            double limit = Guards.requireFinite(in.limit(), "limit", "Límite de comparación");
            if (limit <= 0 || !Double.isFinite(limit)) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "limit",
                "El límite debe ser positivo y finito (0 < c < ∞).");
            }
            String mode = in.mode() == null ? "" : in.mode().trim().toLowerCase(java.util.Locale.ROOT);
            String conclusion = switch (mode) {
            case "mayorante-convergente" -> "la serie de aₙ converge";
            case "minorante-divergente" -> "la serie de aₙ diverge";
            default -> throw new CalcException(ErrorCode.INVALID_INPUT, "mode",
                "Usa mayorante-convergente o minorante-divergente.");
            };
            return result(limit, "Comparación por límite", "lim(aₙ/bₙ) = c = " + limit,
                List.of("Supuesto: aₙ y bₙ son no negativos desde algún índice.",
                    mode.equals("mayorante-convergente")
                        ? "Σbₙ converge y aₙ ≤ K·bₙ eventualmente."
                        : "Σbₙ diverge y aₙ ≥ K·bₙ eventualmente.",
                    "Como 0 < c < ∞, ambas series tienen el mismo carácter:", conclusion + "."));
        }
        }

    public static final class SimpleLimit implements Calculator<LimitInput> {
        public static final String FORMULA_ID = "series.limit";
        private static final FormulaDef DEF = def(FORMULA_ID, "Límite de función", "lim f(x), x→a",
                List.of(InputDef.text("expression", "Función f(x)", "Ej. (x^2-1)/(x-1)"),
                        InputDef.text("variable", "Variable", "x"), number("point", "Punto a")), "Límite");
        private final com.mathedu.domain.engine.SymbolicEngine symbolic;
        private final ExpressionEngine numeric;

        public SimpleLimit(com.mathedu.domain.engine.SymbolicEngine symbolic) {
            this(symbolic, null);
        }

        /**
         * @param numeric motor numerico opcional; si falta, un limite
         *                indeterminado se informa como error en lugar de
         *                resolverse por aproximacion bilateral.
         */
        public SimpleLimit(com.mathedu.domain.engine.SymbolicEngine symbolic,
                           ExpressionEngine numeric) {
            this.symbolic = Objects.requireNonNull(symbolic);
            this.numeric = numeric;
        }

        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(LimitInput in) throws CalcException {
            requireInput(in);
            String variable = in.variable() == null ? "" : in.variable().trim();
            Guards.requireFinite(in.point(), "point", "Punto a");
            try {
                double value = Guards.requireFiniteResult(
                        symbolic.limit(in.expression(), variable, in.point()));
                return result(value, "Límite", "lim[x→" + in.point() + "] " + in.expression(), List.of(
                        "Se evalúa el límite cuando " + variable + " tiende a " + in.point() + ".",
                        "Resultado: " + value));
            } catch (CalcException symbolicFailure) {
                if (numeric == null) {
                    throw symbolicFailure;
                }
                double value = limitByApproach(numeric, in.expression(), variable, in.point());
                return new CalculationResult(value, "Límite (aproximación)",
                        "lim[x→" + in.point() + "] " + in.expression(),
                        List.of("El cálculo simbólico no resolvió el límite: "
                                        + symbolicFailure.getMessage(),
                                "Se aproxima f(x) por ambos lados con x = " + in.point() + " ± h.",
                                "Límite ≈ " + value),
                        "", false,
                        "Límite numérico aproximado; puede no existir si f no es continua.");
            }
        }

        /** Media de los valores a izquierda y derecha del punto. */
        private static double limitByApproach(ExpressionEngine engine, String expression,
                                              String variable, double point) throws CalcException {
            if (variable.isEmpty()) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "expression",
                        "Indica la variable del límite para poder aproximarlo numéricamente.");
            }
            double h = Math.max(Math.abs(point) * 1e-6, 1e-8);
            double left = engine.evaluate(expression, Map.of(variable, point - h));
            double right = engine.evaluate(expression, Map.of(variable, point + h));
            return Guards.requireFiniteResult((left + right) / 2);
        }
    }

    private static CalculationResult criterion(CriterionInput in, String formula, String name)
            throws CalcException {
        requireInput(in);
        double limit = Guards.requireFinite(in.limit(), "limit", "Límite");
        String conclusion = limit < 1 ? "converge absolutamente" : limit > 1
                ? "diverge" : "inconcluso (L = 1)";
        return result(limit, "Límite " + name, formula, List.of(
                "L = " + limit,
                "Conclusión del criterio " + name + ": la serie " + conclusion + "."));
    }

    private static FormulaDef criterionDef(String id, String name) {
        return def(id, name, "L < 1: converge; L > 1: diverge; L = 1: inconcluso", List.of(
                number("limit", "Límite L")), "Conclusión");
    }

    private static FormulaDef def(String id, String name, String expression,
                                  List<InputDef> inputs, String label) {
        return new FormulaDef(id, name, CATEGORY, expression, inputs, label, "");
    }

    private static InputDef number(String key, String label) {
        return new InputDef(key, label, "Ej. 2", 0, 0, true);
    }

    private static InputDef integer(String key, String label) {
        return new InputDef(key, label, "Ej. 5", 0, 0, true);
    }

    private static void requireTerms(int terms) throws CalcException {
        if (terms < 1 || terms > 100000) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "terms",
                    "n debe estar entre 1 y 100000.");
        }
    }

    private static void requireInput(Object input) throws CalcException {
        if (input == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacíos.");
        }
    }

    private static CalculationResult result(double value, String label, String formula,
                                            List<String> steps) throws CalcException {
        return new CalculationResult(Guards.requireFiniteResult(value), label, formula,
                steps, "", true, null);
    }
}