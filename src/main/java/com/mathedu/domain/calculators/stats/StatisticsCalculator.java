package com.mathedu.domain.calculators.stats;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.Arrays;
import java.util.List;

/** Calculadores de estadistica, conteo y distribuciones clasicas. */
public final class StatisticsCalculator {

    private static final String CATEGORY = "Estadística y probabilidad";

    private StatisticsCalculator() {
    }

    public static final class Descriptive implements Calculator<Descriptive.Input> {
        public static final String FORMULA_ID = "stats.descriptive";
        public record Input(String data, double percentile) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Resumen estadístico",
                "media, mediana, moda, varianza, desviación, rango y percentil",
                List.of(InputDef.text("data", "Datos (separados por coma o punto y coma)",
                        "Ej. 2, 4, 4, 6"),
                        InputDef.optional("percentile", "Percentil (0–100; predeterminado 50)", "Ej. 75")),
                "Media");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double[] values = parseData(in.data());
            if (values.length == 0) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "data", "Introduce al menos un dato.");
            }
            double p = finite(in.percentile(), "percentile", "Percentil");
            if (p < 0 || p > 100) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "percentile",
                        "El percentil debe estar entre 0 y 100.");
            }
            DescriptiveStatistics stats = new DescriptiveStatistics(values);
            double[] modes = StatUtils.mode(values);
            String modeText = modes.length == 0 ? "sin moda" : Arrays.toString(modes);
            double percentile = stats.getPercentile(p == 0 ? 0.01 : p);
            if (p == 0) {
                percentile = stats.getMin();
            }
            List<String> steps = List.of(
                    "Datos: " + Arrays.toString(values) + " (n = " + values.length + ")",
                    "Media: " + stats.getMean(),
                    "Mediana: " + stats.getPercentile(50),
                    "Moda(s): " + modeText,
                    "Varianza muestral: " + stats.getVariance(),
                    "Desviación estándar muestral: " + stats.getStandardDeviation(),
                    "Rango: " + stats.getMin() + " a " + stats.getMax()
                            + " (amplitud " + (stats.getMax() - stats.getMin()) + ")",
                    "Percentil " + p + ": " + percentile);
            return result(stats.getMean(), "Media", "Resumen estadístico descriptivo", steps);
        }
    }

    public static final class Combination implements Calculator<Combination.Input> {
        public static final String FORMULA_ID = "stats.combination";
        public record Input(int n, int r) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Combinaciones",
                "C(n,r) = n! / (r!(n-r)!)", List.of(integer("n", "Elementos n"),
                        integer("r", "Seleccionados r")), "Cantidad de combinaciones");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            validateSelection(in.n(), in.r());
            double value = Guards.requireFiniteResult(
                    CombinatoricsUtils.binomialCoefficientDouble(in.n(), in.r()));
            return result(value, "Combinaciones", "C(n,r) = n! / (r!(n-r)!)", List.of(
                    "Se eligen " + in.r() + " elementos de " + in.n() + " sin importar el orden.",
                    "C(" + in.n() + "," + in.r() + ") = " + value));
        }
    }

    public static final class Permutation implements Calculator<Permutation.Input> {
        public static final String FORMULA_ID = "stats.permutation";
        public record Input(int n, int r) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Variaciones sin repetición",
                "P(n,r) = n! / (n-r)!", List.of(integer("n", "Elementos n"),
                        integer("r", "Seleccionados r")), "Cantidad de variaciones");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            validateSelection(in.n(), in.r());
            double value = CombinatoricsUtils.factorialDouble(in.n())
                    / CombinatoricsUtils.factorialDouble(in.n() - in.r());
            value = Guards.requireFiniteResult(value);
            return result(value, "Variaciones", "P(n,r) = n! / (n-r)!", List.of(
                    "El orden importa y no se repiten elementos.",
                    "P(" + in.n() + "," + in.r() + ") = " + value));
        }
    }

    public static final class BasicProbability implements Calculator<BasicProbability.Input> {
        public static final String FORMULA_ID = "stats.probability";
        public record Input(double favorable, double total) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Probabilidad clásica",
                "P(A) = casos favorables / casos posibles", List.of(
                        number("favorable", "Casos favorables"), number("total", "Casos posibles")),
                "Probabilidad");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double favorable = finite(in.favorable(), "favorable", "Casos favorables");
            double total = Guards.requirePositive(in.total(), "total", "Casos posibles");
            if (favorable < 0 || favorable > total) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "favorable",
                        "Los casos favorables deben estar entre 0 y el total.");
            }
            double p = favorable / total;
            return result(p, "Probabilidad", "P(A) = favorables / posibles", List.of(
                    "P(A) = " + favorable + " / " + total + " = " + p,
                    "Porcentaje: " + (100 * p) + "%"));
        }
    }

    public static final class ConditionalProbability implements Calculator<ConditionalProbability.Input> {
        public static final String FORMULA_ID = "stats.conditional-probability";
        public record Input(double joint, double condition) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Probabilidad condicional",
                "P(A|B) = P(A∩B) / P(B)", List.of(
                        number("joint", "P(A y B)"), number("condition", "P(B)")), "P(A|B)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double joint = finite(in.joint(), "joint", "P(A y B)");
            double condition = finite(in.condition(), "condition", "P(B)");
            if (condition <= 0 || condition > 1 || joint < 0 || joint > condition) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "condition",
                        "Se requiere 0 < P(B) ≤ 1 y 0 ≤ P(A∩B) ≤ P(B).");
            }
            double p = joint / condition;
            return result(p, "P(A|B)", "P(A|B) = P(A∩B) / P(B)", List.of(
                    "P(A|B) = " + joint + " / " + condition + " = " + p));
        }
    }

    public static final class Binomial implements Calculator<Binomial.Input> {
        public static final String FORMULA_ID = "stats.binomial";
        public record Input(int trials, int successes, double probability) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Distribución binomial",
                "P(X=k) = C(n,k)·p^k·(1-p)^(n-k)", List.of(
                        integer("n", "Ensayos n"), integer("k", "Éxitos k"),
                        number("p", "Probabilidad de éxito p")), "P(X=k)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            if (in.trials() < 0 || in.trials() > 100000 || in.successes() < 0
                    || in.successes() > in.trials()) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "n",
                        "Se requiere 0 ≤ k ≤ n ≤ 100000.");
            }
            double p = finite(in.probability(), "p", "Probabilidad");
            if (p < 0 || p > 1) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "p", "p debe estar entre 0 y 1.");
            }
            double value = new BinomialDistribution(in.trials(), p).probability(in.successes());
            return result(value, "P(X=k)", "Distribución binomial", List.of(
                    "Ensayos n = " + in.trials() + ", éxitos k = " + in.successes() + ", p = " + p,
                    "P(X = " + in.successes() + ") = " + value));
        }
    }

    public static final class Normal implements Calculator<Normal.Input> {
        public static final String FORMULA_ID = "stats.normal";
        public record Input(double mean, double standardDeviation, double value) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Distribución normal acumulada",
                "P(X≤x) para X ~ N(μ,σ)", List.of(number("mean", "Media μ"),
                        InputDef.positive("sd", "Desviación estándar σ", "Ej. 1.5"),
                        number("x", "Valor x")), "P(X≤x)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double mean = finite(in.mean(), "mean", "Media");
            double sd = Guards.requirePositive(in.standardDeviation(), "sd", "Desviación estándar");
            double x = finite(in.value(), "x", "Valor x");
            double probability = new NormalDistribution(mean, sd).cumulativeProbability(x);
            return result(probability, "P(X≤x)", "Distribución normal acumulada", List.of(
                    "X ~ N(" + mean + ", " + sd + ")",
                    "P(X ≤ " + x + ") = " + probability));
        }
    }

    public static final class Poisson implements Calculator<Poisson.Input> {
        public static final String FORMULA_ID = "stats.poisson";
        public record Input(double mean, int occurrences) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Distribución de Poisson",
                "P(X=k) = e^-λ·λ^k/k!", List.of(
                        InputDef.positive("mean", "Media λ", "Ej. 3.5"),
                        integer("k", "Ocurrencias k")), "P(X=k)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double mean = Guards.requirePositive(in.mean(), "mean", "Media λ");
            if (in.occurrences() < 0) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "k", "k debe ser mayor o igual a cero.");
            }
            double probability = new PoissonDistribution(mean).probability(in.occurrences());
            return result(probability, "P(X=k)", "Distribución de Poisson", List.of(
                    "λ = " + mean + ", k = " + in.occurrences(),
                    "P(X = " + in.occurrences() + ") = " + probability));
        }
    }

    private static FormulaDef def(String id, String name, String expression,
                                  List<InputDef> inputs, String result) {
        return new FormulaDef(id, name, CATEGORY, expression, inputs, result, "");
    }

    private static InputDef number(String key, String label) {
        return new InputDef(key, label, "Ej. 2", 0, 0, true);
    }

    private static InputDef integer(String key, String label) {
        return new InputDef(key, label, "Ej. 5", 0, 0, true);
    }

    private static void validateSelection(int n, int r) throws CalcException {
        if (n < 0 || n > 170 || r < 0 || r > n) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "n",
                    "Se requiere 0 ≤ r ≤ n ≤ 170 para mantener resultados finitos.");
        }
    }

    private static double[] parseData(String raw) throws CalcException {
        if (raw == null || raw.isBlank()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "data", "Introduce una lista de datos.");
        }
        String[] tokens = raw.trim().split("[,;\\s]+");
        double[] values = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            values[i] = finite(parseToken(tokens[i]), "data", "Dato " + (i + 1));
        }
        return values;
    }

    private static double parseToken(String token) throws CalcException {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "data",
                    "«" + token + "» no es un dato numérico; separa valores con coma o punto y coma.");
        }
    }

    private static double finite(double value, String field, String label) throws CalcException {
        return Guards.requireFinite(value, field, label);
    }

    private static void requireInput(Object input) throws CalcException {
        if (input == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
    }

    private static CalculationResult result(double value, String label, String formula,
                                            List<String> steps) throws CalcException {
        return new CalculationResult(Guards.requireFiniteResult(value), label, formula, steps,
                "", true, null);
    }
}