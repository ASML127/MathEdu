package com.mathedu.domain.calculators.analytic;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/** Calculadores de geometria analitica con pasos descriptivos. */
public final class AnalyticGeometryCalculator {

    private static final String CATEGORY = "Geometría analítica";

    private AnalyticGeometryCalculator() {
    }

    private static FormulaDef def(String id, String name, String expression,
                                  List<InputDef> inputs, String result) {
        return new FormulaDef(id, name, CATEGORY, expression, inputs, result, "");
    }

    private static double finite(double value, String key, String label) throws CalcException {
        return Guards.requireFinite(value, key, label);
    }

    public static final class Distance implements Calculator<Distance.Input> {
        public static final String FORMULA_ID = "analytic.distance";
        public record Input(double x1, double y1, double x2, double y2) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Distancia entre dos puntos",
                "d = √((x₂-x₁)² + (y₂-y₁)²)", coordinates(), "Distancia");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double dx = finite(in.x2() - in.x1(), "x2", "Diferencia x");
            double dy = finite(in.y2() - in.y1(), "y2", "Diferencia y");
            double distance = Guards.requireFiniteResult(Math.hypot(dx, dy));
            return result(distance, "Distancia", "d = √((x₂-x₁)² + (y₂-y₁)²)", List.of(
                    "Δx = " + in.x2() + " - " + in.x1() + " = " + dx,
                    "Δy = " + in.y2() + " - " + in.y1() + " = " + dy,
                    "d = √(" + dx + "² + " + dy + "²) = " + distance));
        }
    }

    public static final class Midpoint implements Calculator<Midpoint.Input> {
        public static final String FORMULA_ID = "analytic.midpoint";
        public record Input(double x1, double y1, double x2, double y2) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Punto medio",
                "M = ((x₁+x₂)/2, (y₁+y₂)/2)", coordinates(), "Coordenada x de M");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double x = finite((in.x1() + in.x2()) / 2, "x1", "Coordenada x");
            double y = finite((in.y1() + in.y2()) / 2, "y1", "Coordenada y");
            return result(x, "Punto medio", "M = ((x₁+x₂)/2, (y₁+y₂)/2)", List.of(
                    "xM = (" + in.x1() + " + " + in.x2() + ")/2 = " + x,
                    "yM = (" + in.y1() + " + " + in.y2() + ")/2 = " + y,
                    "Punto medio: M = (" + x + ", " + y + ")"));
        }
    }

    public static final class Slope implements Calculator<Slope.Input> {
        public static final String FORMULA_ID = "analytic.slope";
        public record Input(double x1, double y1, double x2, double y2) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Pendiente de una recta",
                "m = (y₂-y₁)/(x₂-x₁)", coordinates(), "Pendiente m");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double dx = finite(in.x2() - in.x1(), "x2", "Diferencia x");
            double dy = finite(in.y2() - in.y1(), "y2", "Diferencia y");
            if (dx == 0) {
                throw new CalcException(ErrorCode.DOMAIN_ERROR, "x2",
                        "La recta es vertical y su pendiente no esta definida.");
            }
            double slope = Guards.requireFiniteResult(dy / dx);
            return result(slope, "Pendiente m", "m = (y₂-y₁)/(x₂-x₁)", List.of(
                    "m = (" + in.y2() + " - " + in.y1() + ")/(" + in.x2() + " - " + in.x1() + ")",
                    "Pendiente: m = " + slope));
        }
    }

    public static final class LineFromPoints implements Calculator<LineFromPoints.Input> {
        public static final String FORMULA_ID = "analytic.line-points";
        public record Input(double x1, double y1, double x2, double y2) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Recta por dos puntos",
                "y-y₁ = m(x-x₁)", coordinates(), "Ecuación de la recta");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double dx = finite(in.x2() - in.x1(), "x2", "Diferencia x");
            double dy = finite(in.y2() - in.y1(), "y2", "Diferencia y");
            if (dx == 0 && dy == 0) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "x2",
                        "Se necesitan dos puntos distintos para definir una recta.");
            }
            String equation;
            double value;
            if (dx == 0) {
                equation = "x = " + in.x1();
                value = in.x1();
            } else {
                double m = dy / dx;
                                double b = Guards.requireFiniteResult(in.y1() - m * in.x1());
                                m = Guards.requireFiniteResult(m);
                equation = "y = " + m + "x + " + b;
                value = Guards.requireFiniteResult(m);
            }
            return result(value, "Ecuación de la recta", equation, List.of(
                    "Puntos: (" + in.x1() + ", " + in.y1() + ") y (" + in.x2() + ", " + in.y2() + ")",
                    dx == 0 ? "Recta vertical: x = " + in.x1()
                            : "m = (" + dy + ")/" + dx + " = " + value + "; b = y₁ - mx₁",
                    "Forma pendiente-ordenada: " + equation,
                    dx == 0 ? "Forma general: x - " + in.x1() + " = 0"
                            : "Forma general: " + value + "x - y + " + (in.y1() - value * in.x1()) + " = 0"));
        }
    }

    public static final class PointSlope implements Calculator<PointSlope.Input> {
        public static final String FORMULA_ID = "analytic.line-point-slope";
        public record Input(double x1, double y1, double slope) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Recta en forma punto-pendiente",
                "y-y₁ = m(x-x₁)", List.of(number("x1", "Coordenada x₁"),
                        number("y1", "Coordenada y₁"), number("m", "Pendiente m")),
                "Ordenada al origen");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double x = finite(in.x1(), "x1", "Coordenada x₁");
            double y = finite(in.y1(), "y1", "Coordenada y₁");
            double m = finite(in.slope(), "m", "Pendiente");
            double intercept = Guards.requireFiniteResult(y - m * x);
            String pointSlope = "y - " + y + " = " + m + "(x - " + x + ")";
            String slopeIntercept = "y = " + m + "x + " + intercept;
            String general = format(m) + "x - y + " + format(intercept) + " = 0";
            return result(intercept, "Ordenada al origen", slopeIntercept, List.of(
                    "Forma punto-pendiente: " + pointSlope,
                    "Despeje: y = mx + (y₁ - mx₁)",
                    "Forma pendiente-ordenada: " + slopeIntercept,
                    "Forma general: " + general));
        }
    }

    public static final class LineFromSlopeIntercept implements Calculator<LineFromSlopeIntercept.Input> {
        public static final String FORMULA_ID = "analytic.line-slope-intercept";
        public record Input(double slope, double intercept) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Convertir recta pendiente-ordenada",
                "y = mx + b", List.of(number("m", "Pendiente m"),
                        number("b", "Ordenada al origen b")), "Forma general");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double m = finite(in.slope(), "m", "Pendiente");
            double b = finite(in.intercept(), "b", "Ordenada");
            String general = format(m) + "x - y + " + format(b) + " = 0";
            return result(m, "Forma general", general, List.of(
                    "Forma pendiente-ordenada: y = " + m + "x + " + b,
                    "Pasa y al primer miembro y obtiene Ax + By + C = 0.",
                    "Forma general: " + general));
        }
    }

    public static final class GeneralLineConversion implements Calculator<GeneralLineConversion.Input> {
        public static final String FORMULA_ID = "analytic.line-general";
        public record Input(double a, double b, double c) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Convertir recta general",
                "Ax + By + C = 0", List.of(number("a", "Coeficiente A"),
                        number("b", "Coeficiente B"), number("c", "Coeficiente C")),
                "Pendiente o corte vertical");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double a = finite(in.a(), "a", "Coeficiente A");
            double b = finite(in.b(), "b", "Coeficiente B");
            double c = finite(in.c(), "c", "Coeficiente C");
            if (a == 0 && b == 0) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "a",
                        "A y B no pueden ser ambos cero en una ecuacion de recta.");
            }
            if (b == 0) {
                                double x = Guards.requireFiniteResult(-c / a);
                return result(x, "Corte vertical x", "x = " + x, List.of(
                        "Recta vertical: x = -C/A = " + x,
                        "No existe forma y = mx + b para una recta vertical."));
            }
            double m = Guards.requireFiniteResult(-a / b);
            double intercept = Guards.requireFiniteResult(-c / b);
            return result(m, "Pendiente m", "y = " + m + "x + " + intercept, List.of(
                    "Ax + By + C = 0",
                    "Despeje y: y = (-A/B)x - C/B",
                    "Pendiente-ordenada: y = " + m + "x + " + intercept));
        }
    }

    public static final class LineIntersection implements Calculator<LineIntersection.Input> {
        public static final String FORMULA_ID = "analytic.line-intersection";
        public record Input(double a1, double b1, double c1, double a2, double b2, double c2) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Intersección de rectas",
                "A₁x+B₁y+C₁=0 ; A₂x+B₂y+C₂=0", List.of(
                        number("a1", "A₁"), number("b1", "B₁"), number("c1", "C₁"),
                        number("a2", "A₂"), number("b2", "B₂"), number("c2", "C₂")),
                "Coordenada x de la intersección");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
                        double a1 = finite(in.a1(), "a1", "A₁");
                        double b1 = finite(in.b1(), "b1", "B₁");
                        double c1 = finite(in.c1(), "c1", "C₁");
                        double a2 = finite(in.a2(), "a2", "A₂");
                        double b2 = finite(in.b2(), "b2", "B₂");
                        double c2 = finite(in.c2(), "c2", "C₂");
                        if ((a1 == 0 && b1 == 0) || (a2 == 0 && b2 == 0)) {
                                throw new CalcException(ErrorCode.INVALID_INPUT, "a1",
                                                "Cada recta debe tener A o B distinto de cero.");
                        }
                        double determinant = Guards.requireFiniteResult(a1 * b2 - a2 * b1);
                        if (Math.abs(determinant) < 1e-12) {
                                boolean coincident = Math.abs(a1 * c2 - a2 * c1) < 1e-12
                                                && Math.abs(b1 * c2 - b2 * c1) < 1e-12;
                throw new CalcException(coincident ? ErrorCode.INFINITE_SOLUTIONS : ErrorCode.NO_SOLUTION,
                        coincident ? "Las rectas coinciden: hay infinitos puntos comunes."
                                : "Las rectas son paralelas: no tienen interseccion.");
            }
            double x = Guards.requireFiniteResult((b1 * c2 - b2 * c1) / determinant);
            double y = Guards.requireFiniteResult((c1 * a2 - c2 * a1) / determinant);
            return result(x, "Intersección", "(" + x + ", " + y + ")", List.of(
                    "Sistema: A₁x + B₁y = -C₁; A₂x + B₂y = -C₂",
                    "Determinante: D = A₁B₂ - A₂B₁ = " + determinant,
                    "Regla de Cramer: x = " + x + ", y = " + y,
                    "Punto de intersección: (" + x + ", " + y + ")"));
        }
    }

    public static final class PointLineDistance implements Calculator<PointLineDistance.Input> {
        public static final String FORMULA_ID = "analytic.point-line-distance";
        public record Input(double x, double y, double a, double b, double c) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Distancia de punto a recta",
                "d = |Ax₀+By₀+C|/√(A²+B²)", List.of(number("x", "Coordenada x₀"),
                        number("y", "Coordenada y₀"), number("a", "Coeficiente A"),
                        number("b", "Coeficiente B"), number("c", "Coeficiente C")), "Distancia");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
                        double x = finite(in.x(), "x", "Coordenada x₀");
                        double y = finite(in.y(), "y", "Coordenada y₀");
                        double a = finite(in.a(), "a", "Coeficiente A");
                        double b = finite(in.b(), "b", "Coeficiente B");
                        double c = finite(in.c(), "c", "Coeficiente C");
                        double denominator = Math.hypot(a, b);
            if (denominator == 0) {
                throw new CalcException(ErrorCode.INVALID_INPUT, "a",
                        "A y B no pueden ser ambos cero en la ecuacion de recta.");
            }
            double numerator = Math.abs(a * x + b * y + c);
            numerator = Guards.requireFiniteResult(numerator);
            double distance = Guards.requireFiniteResult(numerator / denominator);
            return result(distance, "Distancia", "d = |Ax₀+By₀+C|/√(A²+B²)", List.of(
                    "Numerador: |" + in.a() + "·" + in.x() + " + " + in.b() + "·" + in.y()
                            + " + " + in.c() + "| = " + numerator,
                    "Denominador: √(A²+B²) = " + denominator,
                    "Distancia perpendicular: d = " + distance));
        }
    }

    public static final class CircleEquation implements Calculator<CircleEquation.Input> {
        public static final String FORMULA_ID = "analytic.circle";
        public record Input(double h, double k, double radius) { }
        private static final FormulaDef DEF = def(FORMULA_ID, "Ecuación de circunferencia",
                "(x-h)² + (y-k)² = r²", List.of(number("h", "Centro h (x)"),
                        number("k", "Centro k (y)"), InputDef.positive("r", "Radio r", "Ej. 5")),
                "Radio");
        @Override public FormulaDef formula() { return DEF; }
        @Override public CalculationResult calculate(Input in) throws CalcException {
            requireInput(in);
            double h = finite(in.h(), "h", "Centro x");
            double k = finite(in.k(), "k", "Centro y");
            double r = Guards.requirePositive(in.radius(), "r", "Radio");
            double r2 = Guards.requireFiniteResult(r * r);
            double area = Guards.requireFiniteResult(Math.PI * r2);
            double circumference = Guards.requireFiniteResult(2 * Math.PI * r);
            String equation = "(x - " + h + ")² + (y - " + k + ")² = " + r2;
            return result(r, "Radio", equation, List.of(
                    "Centro: C = (" + h + ", " + k + ")",
                    "Ecuacion ordinaria: " + equation,
                    "Area: πr² = " + area,
                    "Longitud: 2πr = " + circumference));
        }
    }

    private static List<InputDef> coordinates() {
        return List.of(number("x1", "x₁"), number("y1", "y₁"),
                number("x2", "x₂"), number("y2", "y₂"));
    }

    private static InputDef number(String key, String label) {
        return new InputDef(key, label, "Ej. 3", 0, 0, true);
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

    private static String format(double value) {
        return value == Math.rint(value) ? Long.toString((long) value) : Double.toString(value);
    }
}