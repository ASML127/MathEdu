package com.mathedu.domain.calculators.linear;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.Arrays;
import java.util.List;

/** Calculadores de vectores y matrices construidos sobre Apache Commons Math. */
public final class LinearAlgebraCalculator {

    private static final String CATEGORY = "Álgebra lineal";
    private static final int MAX_DIMENSION = 50;

    private LinearAlgebraCalculator() {
    }

    public record VectorInput(String first, String second) { }
    public record MatrixInput(String first, String second) { }
    public record SingleMatrixInput(String matrix) { }
    public record SystemInput(String matrix, String constants) { }

    public static final class VectorAdd implements Calculator<VectorInput> {
        public static final String FORMULA_ID = "linear.vector-add";
        private static final FormulaDef DEF = def(FORMULA_ID, "Suma de vectores", "u + v",
                List.of(text("first", "Vector u", "Ej. 1, 2, 3"),
                        text("second", "Vector v", "Ej. 4, 5, 6")), "Componente 1");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(VectorInput in) throws CalcException {
            double[][] values = vectorPair(in);
            double[] result = new double[values[0].length];
            for (int i = 0; i < result.length; i++) {
                result[i] = Guards.requireFiniteResult(values[0][i] + values[1][i]);
            }
            return vectorResult(result, "u + v", "Suma componente a componente.");
        }
    }

    public static final class VectorSubtract implements Calculator<VectorInput> {
        public static final String FORMULA_ID = "linear.vector-subtract";
        private static final FormulaDef DEF = def(FORMULA_ID, "Resta de vectores", "u - v",
                List.of(text("first", "Vector u", "Ej. 5, 7, 9"),
                        text("second", "Vector v", "Ej. 1, 2, 3")), "Componente 1");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(VectorInput in) throws CalcException {
            double[][] values = vectorPair(in);
            double[] result = new double[values[0].length];
            for (int i = 0; i < result.length; i++) {
                result[i] = Guards.requireFiniteResult(values[0][i] - values[1][i]);
            }
            return vectorResult(result, "u - v", "Resta componente a componente.");
        }
    }

    public static final class VectorDotProduct implements Calculator<VectorInput> {
        public static final String FORMULA_ID = "linear.vector-dot";
        private static final FormulaDef DEF = def(FORMULA_ID, "Producto escalar", "u · v",
                List.of(text("first", "Vector u", "Ej. 1, 2, 3"),
                        text("second", "Vector v", "Ej. 4, 5, 6")), "Producto escalar");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(VectorInput in) throws CalcException {
            double[][] values = vectorPair(in);
            double dot = 0;
            for (int i = 0; i < values[0].length; i++) {
                dot = Guards.requireFiniteResult(dot + values[0][i] * values[1][i]);
            }
            return result(dot, "Producto escalar", "u · v = Σ uᵢvᵢ", List.of(
                    "u = " + Arrays.toString(values[0]),
                    "v = " + Arrays.toString(values[1]),
                    "u · v = " + dot));
        }
    }

    public static final class MatrixAdd implements Calculator<MatrixInput> {
        public static final String FORMULA_ID = "linear.matrix-add";
        private static final FormulaDef DEF = matrixBinaryDef(FORMULA_ID, "Suma de matrices", "A + B");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(MatrixInput in) throws CalcException {
            return matrixBinary(in, true, DEF);
        }
    }

    public static final class MatrixSubtract implements Calculator<MatrixInput> {
        public static final String FORMULA_ID = "linear.matrix-subtract";
        private static final FormulaDef DEF = matrixBinaryDef(FORMULA_ID, "Resta de matrices", "A - B");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(MatrixInput in) throws CalcException {
            return matrixBinary(in, false, DEF);
        }
    }

    public static final class MatrixProduct implements Calculator<MatrixInput> {
        public static final String FORMULA_ID = "linear.matrix-product";
        private static final FormulaDef DEF = matrixBinaryDef(FORMULA_ID,
                "Producto de matrices", "A · B");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(MatrixInput in) throws CalcException {
            requireInput(in);
            RealMatrix left = matrix(in.first(), "first");
            RealMatrix right = matrix(in.second(), "second");
            if (left.getColumnDimension() != right.getRowDimension()) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "second",
                        "Las columnas de A deben coincidir con las filas de B.");
            }
            RealMatrix product = left.multiply(right);
            return matrixResult(product, "Producto A·B", "C = A · B",
                    "Cᵢⱼ = Σ AᵢₖBₖⱼ.");
        }
    }

    public static final class Determinant implements Calculator<SingleMatrixInput> {
        public static final String FORMULA_ID = "linear.determinant";
        private static final FormulaDef DEF = matrixUnaryDef(FORMULA_ID, "Determinante", "det(A)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(SingleMatrixInput in) throws CalcException {
            RealMatrix matrix = squareMatrix(in);
            double determinant = Guards.requireFiniteResult(new LUDecomposition(matrix).getDeterminant());
            return result(determinant, "Determinante", "det(A)", List.of(
                    "Matriz A: " + matrixText(matrix),
                    "det(A) = " + determinant));
        }
    }

    public static final class Inverse implements Calculator<SingleMatrixInput> {
        public static final String FORMULA_ID = "linear.inverse";
        private static final FormulaDef DEF = matrixUnaryDef(FORMULA_ID, "Matriz inversa", "A⁻¹");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(SingleMatrixInput in) throws CalcException {
            RealMatrix matrix = squareMatrix(in);
            DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
            if (!solver.isNonSingular()) {
                throw new CalcException(ErrorCode.SINGULAR_MATRIX, "matrix",
                        "La matriz es singular y no tiene inversa.");
            }
            RealMatrix inverse = solver.getInverse();
            return matrixResult(inverse, "Elemento [1,1] de A⁻¹", "A⁻¹",
                    "Comprobacion: A·A⁻¹ = I.");
        }
    }

    public static final class SolveGauss implements Calculator<SystemInput> {
        public static final String FORMULA_ID = "linear.gauss";
        private static final FormulaDef DEF = def(FORMULA_ID, "Resolver sistema por Gauss",
                "Ax = b", List.of(text("matrix", "Matriz de coeficientes A", "Ej. 2,1;1,-1"),
                        text("constants", "Vector de términos independientes b", "Ej. 5,1")),
                "Primera incógnita");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(SystemInput in) throws CalcException {
            requireInput(in);
            RealMatrix coefficients = matrix(in.matrix(), "matrix");
            double[] constants = parseVector(in.constants(), "constants");
            if (coefficients.getRowDimension() != coefficients.getColumnDimension()
                    || constants.length != coefficients.getRowDimension()) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "matrix",
                        "Gauss requiere A cuadrada y un término independiente por ecuación.");
            }
            DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
            if (!solver.isNonSingular()) {
                throw new CalcException(ErrorCode.SINGULAR_MATRIX, "matrix",
                        "El sistema es singular o no tiene una solución única.");
            }
            double[] solution = solver.solve(new org.apache.commons.math3.linear.ArrayRealVector(
                    constants, true)).toArray();
            for (double value : solution) {
                Guards.requireFiniteResult(value);
            }
            return result(solution[0], "Primera incógnita", "Ax = b (eliminación de Gauss)", List.of(
                    "Matriz aumentada [A|b]: " + matrixText(coefficients) + " | "
                            + Arrays.toString(constants),
                    "Eliminación con pivoteo parcial y sustitución regresiva.",
                    "Solución x = " + Arrays.toString(solution)));
        }
    }

    public static final class Rank implements Calculator<SingleMatrixInput> {
        public static final String FORMULA_ID = "linear.rank";
        private static final FormulaDef DEF = matrixUnaryDef(FORMULA_ID, "Rango de matriz", "rank(A)");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(SingleMatrixInput in) throws CalcException {
            RealMatrix matrix = matrixFrom(in);
            int rank = new SingularValueDecomposition(matrix).getRank();
            return result(rank, "Rango", "rank(A)", List.of(
                    "Matriz A: " + matrixText(matrix),
                    "Rango de A = " + rank));
        }
    }

    private static FormulaDef def(String id, String name, String expression,
                                  List<InputDef> inputs, String label) {
        return new FormulaDef(id, name, CATEGORY, expression, inputs, label, "");
    }

    private static InputDef text(String key, String label, String placeholder) {
        return InputDef.text(key, label, placeholder);
    }

    private static FormulaDef matrixBinaryDef(String id, String name, String expression) {
        return def(id, name, expression, List.of(
                text("first", "Matriz A", "Ej. 1,2;3,4"),
                text("second", "Matriz B", "Ej. 5,6;7,8")), "Elemento [1,1]");
    }

    private static FormulaDef matrixUnaryDef(String id, String name, String expression) {
        return def(id, name, expression,
                List.of(text("matrix", "Matriz A", "Ej. 1,2;3,4")), "Resultado");
    }

    private static double[][] vectorPair(VectorInput in) throws CalcException {
        requireInput(in);
        double[] left = parseVector(in.first(), "first");
        double[] right = parseVector(in.second(), "second");
        if (left.length != right.length) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "second",
                    "Los vectores deben tener la misma cantidad de componentes.");
        }
        return new double[][]{left, right};
    }

    private static double[] parseVector(String raw, String field) throws CalcException {
        if (raw == null || raw.isBlank()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field, "Introduce los componentes.");
        }
        String[] tokens = raw.trim().split("[,;\\s]+");
        if (tokens.length > MAX_DIMENSION) {
            throw new CalcException(ErrorCode.INVALID_RANGE, field,
                    "El vector supera el maximo de " + MAX_DIMENSION + " componentes.");
        }
        double[] values = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            values[i] = parseNumber(tokens[i], field);
        }
        return values;
    }

    private static RealMatrix squareMatrix(SingleMatrixInput in) throws CalcException {
        RealMatrix matrix = matrixFrom(in);
        if (matrix.getRowDimension() != matrix.getColumnDimension()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "matrix",
                    "Esta operación requiere una matriz cuadrada.");
        }
        return matrix;
    }

    private static RealMatrix matrixFrom(SingleMatrixInput in) throws CalcException {
        requireInput(in);
        return matrix(in.matrix(), "matrix");
    }

    private static RealMatrix matrix(String raw, String field) throws CalcException {
        if (raw == null || raw.isBlank()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field, "Introduce una matriz.");
        }
        String[] rows = raw.trim().split(";");
        if (rows.length > MAX_DIMENSION) {
            throw new CalcException(ErrorCode.INVALID_RANGE, field,
                    "La matriz supera el maximo de " + MAX_DIMENSION + " filas.");
        }
        double[][] values = new double[rows.length][];
        int columns = -1;
        for (int row = 0; row < rows.length; row++) {
            String text = rows[row].trim();
            if (text.isEmpty()) {
                throw new CalcException(ErrorCode.INVALID_INPUT, field,
                        "La fila " + (row + 1) + " esta vacia.");
            }
            String[] tokens = text.split("[,\\s]+");
            if (columns < 0) {
                columns = tokens.length;
                if (columns == 0 || columns > MAX_DIMENSION) {
                    throw new CalcException(ErrorCode.INVALID_RANGE, field,
                            "La matriz supera el maximo de " + MAX_DIMENSION + " columnas.");
                }
            } else if (tokens.length != columns) {
                throw new CalcException(ErrorCode.INVALID_INPUT, field,
                        "La matriz debe ser rectangular; revisa la fila " + (row + 1) + ".");
            }
            values[row] = new double[columns];
            for (int column = 0; column < columns; column++) {
                values[row][column] = parseNumber(tokens[column], field);
            }
        }
        return new Array2DRowRealMatrix(values, false);
    }

    private static double parseNumber(String token, String field) throws CalcException {
        try {
            double value = Double.parseDouble(token);
            return Guards.requireFinite(value, field, "Componente");
        } catch (NumberFormatException e) {
            throw new CalcException(ErrorCode.INVALID_INPUT, field,
                    "«" + token + "» no es un numero valido de la matriz.");
        }
    }

    private static CalculationResult matrixBinary(MatrixInput in, boolean add, FormulaDef def)
            throws CalcException {
        requireInput(in);
        RealMatrix left = matrix(in.first(), "first");
        RealMatrix right = matrix(in.second(), "second");
        if (left.getRowDimension() != right.getRowDimension()
                || left.getColumnDimension() != right.getColumnDimension()) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "second",
                    "Las matrices deben tener las mismas dimensiones.");
        }
        RealMatrix output = add ? left.add(right) : left.subtract(right);
        return matrixResult(output, def.resultLabel(), def.expressionTemplate(),
                "Operacion elemento a elemento.");
    }

    private static CalculationResult matrixResult(RealMatrix matrix, String label, String formula,
                                                  String detail) throws CalcException {
        double[][] values = matrix.getData();
        for (double[] row : values) {
            for (double value : row) {
                Guards.requireFiniteResult(value);
            }
        }
        return result(values[0][0], label, formula, List.of(
                detail, "Resultado: " + matrixText(matrix)));
    }

    private static CalculationResult vectorResult(double[] values, String formula, String detail)
            throws CalcException {
        for (double value : values) {
            Guards.requireFiniteResult(value);
        }
        return result(values[0], "Componente 1", formula, List.of(
                detail, "Resultado: " + Arrays.toString(values)));
    }

    private static String matrixText(RealMatrix matrix) {
        return Arrays.deepToString(matrix.getData());
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