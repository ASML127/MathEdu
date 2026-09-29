package com.mathedu.domain.algebra;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

import java.util.Arrays;

/**
 * Polinomio con coeficientes reales. Almacenamiento ascendente
 * (indice = grado). El parseo acepta coeficientes en orden
 * descendente, como se escriben: "2, -3, 1" = 2x^2 - 3x + 1.
 * Inmutable.
 */
public final class Polynomial {

    private final double[] asc;

    public Polynomial(double[] ascending) {
        if (ascending == null || ascending.length == 0) {
            throw new IllegalArgumentException("Coeficientes vacios.");
        }
        this.asc = trim(Arrays.copyOf(ascending, ascending.length));
    }

    /** Parsea "2, -3, 1" (descendente) o "2 -3 1". */
    public static Polynomial parse(String raw, String field) throws CalcException {
        String text = raw == null ? "" : raw.trim().replace(',', ' ').replace(';', ' ');
        if (text.isEmpty()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    "Introduce los coeficientes. Ej.: 2, -3, 1  (= 2x^2 - 3x + 1)");
        }
        String[] tokens = text.split("\\s+");
        if (tokens.length > 65) {
            throw new CalcException(ErrorCode.OVERFLOW, field,
                    "Demasiados coeficientes (max 64).");
        }
        double[] desc = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                double v = Double.parseDouble(tokens[i].replace(',', '.'));
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    throw new NumberFormatException();
                }
                desc[i] = v;
            } catch (NumberFormatException e) {
                throw new CalcException(ErrorCode.INVALID_INPUT, field,
                        "«" + tokens[i] + "» no es un coeficiente valido.");
            }
        }
        double[] asc = new double[desc.length];
        for (int i = 0; i < desc.length; i++) {
            asc[i] = desc[desc.length - 1 - i];
        }
        return new Polynomial(asc);
    }

    public static Polynomial of(double... ascending) {
        return new Polynomial(ascending);
    }

    public int degree() {
        return asc.length - 1;
    }

    public boolean isZero() {
        return asc.length == 1 && asc[0] == 0;
    }

    public double leading() {
        return asc[asc.length - 1];
    }

    public double[] coefficientsAsc() {
        return Arrays.copyOf(asc, asc.length);
    }

    /** Evaluacion por Horner. */
    public double evaluate(double x) {
        double acc = 0;
        for (int i = asc.length - 1; i >= 0; i--) {
            acc = acc * x + asc[i];
        }
        return acc;
    }

    public Polynomial add(Polynomial other) {
        int n = Math.max(asc.length, other.asc.length);
        double[] r = new double[n];
        for (int i = 0; i < n; i++) {
            r[i] = coeff(i) + other.coeff(i);
        }
        return new Polynomial(r);
    }

    public Polynomial subtract(Polynomial other) {
        int n = Math.max(asc.length, other.asc.length);
        double[] r = new double[n];
        for (int i = 0; i < n; i++) {
            r[i] = coeff(i) - other.coeff(i);
        }
        return new Polynomial(r);
    }

    public Polynomial multiply(Polynomial other) {
        double[] r = new double[asc.length + other.asc.length - 1];
        for (int i = 0; i < asc.length; i++) {
            for (int j = 0; j < other.asc.length; j++) {
                r[i + j] += asc[i] * other.asc[j];
            }
        }
        return new Polynomial(r);
    }

    /** Division euclidea. El divisor no puede ser el polinomio nulo. */
    public QuotientRemainder divide(Polynomial divisor) throws CalcException {
        if (divisor.isZero()) {
            throw new CalcException(ErrorCode.DIV_BY_ZERO, "coeffB",
                    "Division entre el polinomio nulo.");
        }
        double[] rem = Arrays.copyOf(asc, asc.length);
        double[] quo = new double[Math.max(asc.length - divisor.asc.length + 1, 1)];
        double lead = divisor.leading();
        while (degOf(rem) >= divisor.degree()) {
            int d = degOf(rem) - divisor.degree();
            double c = rem[degOf(rem)] / lead;
            quo[d] += c;
            for (int i = 0; i < divisor.asc.length; i++) {
                rem[d + i] -= c * divisor.asc[i];
            }
        }
        return new QuotientRemainder(new Polynomial(quo), new Polynomial(rem));
    }

    public record QuotientRemainder(Polynomial quotient, Polynomial remainder) {
    }

    /** Forma anidada de Horner: ((a2)x + a1)x + a0. */
    public String hornerForm() {
        StringBuilder sb = new StringBuilder(fmt(asc[asc.length - 1]));
        for (int i = asc.length - 2; i >= 0; i--) {
            sb.insert(0, "(");
            sb.append("*x + ").append(fmt(asc[i])).append(")");
        }
        return sb.toString();
    }

    /** "2x^2 - 3x + 1". Coeficientes enteros se muestran sin decimales. */
    public String toExpressionString() {
        if (isZero()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = asc.length - 1; i >= 0; i--) {
            double c = asc[i];
            if (c == 0) {
                continue;
            }
            String term = termString(Math.abs(c), i);
            if (sb.length() == 0) {
                sb.append(c < 0 ? "-" : "").append(term);
            } else {
                sb.append(c < 0 ? " - " : " + ").append(term);
            }
        }
        return sb.toString();
    }

    private static String termString(double abs, int degree) {
        String coef = (abs == 1 && degree > 0) ? "" : fmt(abs);
        return switch (degree) {
            case 0 -> fmt(abs);
            case 1 -> coef + "x";
            default -> coef + "x^" + degree;
        };
    }

    static String fmt(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) {
            return Long.toString((long) v);
        }
        return Double.toString(v);
    }

    private double coeff(int i) {
        return i < asc.length ? asc[i] : 0;
    }

    private static double[] trim(double[] a) {
        int n = a.length;
        while (n > 1 && a[n - 1] == 0) {
            n--;
        }
        return Arrays.copyOf(a, n);
    }

    private static int degOf(double[] a) {
        int n = a.length - 1;
        while (n > 0 && a[n] == 0) {
            n--;
        }
        return n;
    }
}
