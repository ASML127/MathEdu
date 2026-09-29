package com.mathedu.domain.algebra;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolucion de sistemas lineales con pasos didacticos.
 * 2x2 por Cramer; 3x3 por eliminacion de Gauss con pivoteo parcial.
 */
public final class LinearSystems {

    private static final double EPS = 1e-12;

    private LinearSystems() {
    }

    public record Solution2(double x, double y, double det, List<String> steps) {
    }

    /**
     * a1·x + b1·y = c1 ; a2·x + b2·y = c2.
     * Determinante cero: sin solucion unica (paralelas o coincidentes).
     */
    public static Solution2 solve2x2(double a1, double b1, double c1,
                                     double a2, double b2, double c2) throws CalcException {
        double det = a1 * b2 - a2 * b1;
        List<String> steps = new ArrayList<>();
        steps.add("Sistema: " + fmt(a1) + "x + " + fmt(b1) + "y = " + fmt(c1)
                + " ; " + fmt(a2) + "x + " + fmt(b2) + "y = " + fmt(c2));
        steps.add("Determinante: D = a1*b2 - a2*b1 = " + det);
        if (Math.abs(det) < EPS) {
            boolean proportional = Math.abs(a1 * c2 - a2 * c1) < EPS
                    && Math.abs(b1 * c2 - b2 * c1) < EPS;
            if (proportional) {
                throw new CalcException(ErrorCode.INFINITE_SOLUTIONS,
                        "El sistema tiene infinitas soluciones (rectas coincidentes, D = 0).");
            }
            throw new CalcException(ErrorCode.NO_SOLUTION,
                    "El sistema no tiene solucion (rectas paralelas, D = 0).");
        }
        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        steps.add("Cramer: x = (c1*b2 - c2*b1)/D = " + x);
        steps.add("Cramer: y = (a1*c2 - a2*c1)/D = " + y);
        steps.add("Comprobacion: " + fmt(a1) + "*" + x + " + " + fmt(b1) + "*" + y
                + " = " + (a1 * x + b1 * y));
        return new Solution2(x, y, det, List.copyOf(steps));
    }

    public record Solution3(double x, double y, double z, List<String> steps) {
    }

    /**
     * Sistema 3x3 por Gauss con pivoteo parcial. {@code m} es la matriz
     * aumentada 3x4 por filas.
     */
    public static Solution3 solve3x3(double[][] m) throws CalcException {
        if (m == null || m.length != 3 || m[0].length != 4) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input",
                    "Matriz aumentada 3x4 invalida.");
        }
        double[][] a = new double[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(m[i], 0, a[i], 0, 4);
            for (int j = 0; j < 4; j++) {
                if (Double.isNaN(a[i][j]) || Double.isInfinite(a[i][j])) {
                    throw new CalcException(ErrorCode.INVALID_INPUT, "input",
                            "Coeficientes finitos, por favor.");
                }
            }
        }
        List<String> steps = new ArrayList<>();
        steps.add("Matriz aumentada inicial:");
        steps.add(matrixString(a));

        for (int col = 0; col < 3; col++) {
            int pivot = col;
            for (int row = col + 1; row < 3; row++) {
                if (Math.abs(a[row][col]) > Math.abs(a[pivot][col])) {
                    pivot = row;
                }
            }
            if (Math.abs(a[pivot][col]) < EPS) {
                throw new CalcException(ErrorCode.SINGULAR_MATRIX,
                        "El sistema no tiene solucion unica (pivote nulo en la columna "
                                + (col + 1) + ").");
            }
            if (pivot != col) {
                double[] tmp = a[col];
                a[col] = a[pivot];
                a[pivot] = tmp;
                steps.add("Pivoteo: intercambio F" + (col + 1) + " <-> F" + (pivot + 1));
            }
            for (int row = col + 1; row < 3; row++) {
                double factor = a[row][col] / a[col][col];
                for (int k = col; k < 4; k++) {
                    a[row][k] -= factor * a[col][k];
                }
                if (Math.abs(a[row][col]) < EPS * 1e3) {
                    a[row][col] = 0;
                }
            }
            steps.add("Eliminacion columna " + (col + 1) + ":");
            steps.add(matrixString(a));
        }
        double z = a[2][3] / a[2][2];
        double y = (a[1][3] - a[1][2] * z) / a[1][1];
        double x = (a[0][3] - a[0][2] * z - a[0][1] * y) / a[0][0];
        steps.add("Sustitucion regresiva: z = " + z + ", y = " + y + ", x = " + x);
        return new Solution3(x, y, z, List.copyOf(steps));
    }

    private static String matrixString(double[][] a) {
        StringBuilder sb = new StringBuilder("[ ");
        for (double[] row : a) {
            sb.append("[");
            for (int j = 0; j < row.length; j++) {
                if (j == row.length - 1) {
                    sb.append(" | ");
                } else if (j > 0) {
                    sb.append(", ");
                }
                sb.append(fmt(row[j]));
            }
            sb.append("] ");
        }
        return sb.append("]").toString();
    }

    private static String fmt(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) {
            return Long.toString((long) v);
        }
        return String.format("%.4f", v);
    }
}
