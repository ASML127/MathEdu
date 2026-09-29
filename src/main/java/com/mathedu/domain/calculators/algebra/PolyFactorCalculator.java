package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.algebra.Polynomial;
import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Factoriza polinomios de coeficientes enteros y grado máximo dos sobre R. */
public class PolyFactorCalculator implements Calculator<PolyFactorCalculator.Input> {

    public static final String FORMULA_ID = "arith.poly-factor";

    public record Input(String coefficients) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Factorizar polinomio (grado ≤ 2)",
            "Aritmética y álgebra",
            "P(x) = a(x - r₁)(x - r₂)",
            List.of(InputDef.text("coeffs", "Coeficientes enteros (grado mayor primero)",
                    "Ej. 1, -5, 6")),
            "Factorización",
            "");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        Polynomial polynomial = Polynomial.parse(in.coefficients(), "coeffs");
        if (polynomial.degree() > 2) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "coeffs",
                    "Esta version factoriza polinomios de grado maximo 2.");
        }
        double[] coefficients = polynomial.coefficientsAsc();
        for (double coefficient : coefficients) {
            if (coefficient != Math.rint(coefficient) || Math.abs(coefficient) > 1e9) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "coeffs",
                        "Usa coeficientes enteros con valor absoluto maximo 1e9.");
            }
        }
        if (polynomial.isZero()) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "coeffs",
                    "El polinomio nulo no tiene una factorizacion unica.");
        }

        double leading = polynomial.leading();
        if (polynomial.degree() == 0) {
            return result(leading, polynomial.toExpressionString(), List.of(
                    "Polinomio constante: no tiene factores lineales.",
                    "Factorizacion: " + polynomial.toExpressionString()), true, null);
        }

        double[] roots;
        if (polynomial.degree() == 1) {
            roots = new double[]{-coefficients[0] / leading};
        } else {
            double b = coefficients[1];
            double c = coefficients[0];
            double discriminant = b * b - 4 * leading * c;
            if (discriminant < 0) {
                throw new CalcException(ErrorCode.DOMAIN_ERROR, "coeffs",
                        "El polinomio no se factoriza en factores lineales reales.");
            }
            double sqrt = Math.sqrt(discriminant);
            if (sqrt == 0) {
                roots = new double[]{-b / (2 * leading), -b / (2 * leading)};
            } else {
                double q = -0.5 * (b + Math.copySign(sqrt, b));
                roots = new double[]{q / leading, c / q};
            }
        }

        String factors = format(leading) + factor(roots[0])
                + (roots.length == 2 ? factor(roots[1]) : "");
        List<String> steps = roots.length == 1
                ? List.of("Polinomio: P(x) = " + polynomial.toExpressionString(),
                        "Raiz: x = " + format(roots[0]), "Factorizacion: " + factors)
                : List.of("Polinomio: P(x) = " + polynomial.toExpressionString(),
                        "Discriminante: D = b^2 - 4ac = "
                                + (coefficients[1] * coefficients[1]
                                - 4 * leading * coefficients[0]),
                        "Raices: x₁ = " + format(roots[0]) + ", x₂ = " + format(roots[1]),
                        "Factorizacion: " + factors);
        boolean integerRoots = java.util.Arrays.stream(roots)
                .allMatch(root -> root == Math.rint(root));
        return result(roots[0], factors, steps, integerRoots,
                integerRoots ? null : "Las raices se muestran en aproximacion decimal.");
    }

    private static CalculationResult result(double value, String formula, List<String> steps,
                                            boolean exact, String warning) {
        return new CalculationResult(value, "Factorización", formula, steps, "", exact, warning);
    }

    private static String factor(double root) {
        return "(x - (" + format(root) + "))";
    }

    private static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return Long.toString((long) value);
        }
        return String.format(java.util.Locale.ROOT, "%.8g", value);
    }
}