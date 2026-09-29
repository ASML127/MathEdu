package com.mathedu.domain.calculators.algebra;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;

import java.util.List;

/**
 * Desigualdad lineal: ax + b ? 0 con ? en {>, >=, <, <=}.
 * El valor principal es el punto frontera; el intervalo va en los pasos.
 */
public class InequalityCalculator implements Calculator<InequalityCalculator.Input> {

    public static final String FORMULA_ID = "arith.inequality";

    public record Input(double a, double b, String relation) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Desigualdad lineal",
            "Aritmética y álgebra",
            "ax + b ? 0  (? : > >= < <=)",
            List.of(
                    new InputDef("a", "Coeficiente a", "Ej. 2", 0, 0, true),
                    new InputDef("b", "Término b", "Ej. -4", 0, 0, true),
                    InputDef.text("rel", "Relación (> >= < <=)", "Ej. >=")),
            "Frontera x₀",
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
        double a = Guards.requireFinite(in.a(), "a", "Coeficiente a");
        double b = Guards.requireFinite(in.b(), "b", "Termino b");
        String rel = normalize(in.relation());
        if (!rel.equals(">") && !rel.equals(">=") && !rel.equals("<") && !rel.equals("<=")) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "rel",
                    "Relacion '" + in.relation() + "' no valida. Usa > >= < <=");
        }
        if (a == 0) {
            boolean holds = switch (rel) {
                case ">" -> b > 0;
                case ">=" -> b >= 0;
                case "<" -> b < 0;
                default -> b <= 0;
            };
            if (holds) {
                throw new CalcException(ErrorCode.INFINITE_SOLUTIONS,
                        b + " " + rel + " 0 es siempre cierto: toda la recta real es solucion.");
            }
            throw new CalcException(ErrorCode.NO_SOLUTION,
                    b + " " + rel + " 0 es siempre falso: conjunto vacio.");
        }
        double x0 = Guards.requireFiniteResult(-b / a);
        String interval = solveInterval(a, rel, x0);
        List<String> steps = List.of(
                "Desigualdad: " + a + "x + (" + b + ") " + rel + " 0",
                "Frontera: x0 = -b/a = " + x0,
                a > 0 ? "a > 0: el sentido se conserva."
                        : "a < 0: al despejar se invierte el sentido.",
                "Solucion: " + interval);
        return new CalculationResult(x0, "Frontera x₀",
                "ax + b " + rel + " 0", steps, "", true, null);
    }

    private static String solveInterval(double a, String rel, double x0) {
        // Normaliza a coeficiente positivo (invirtiendo el sentido si a < 0).
        String r = rel;
        if (a < 0) {
            r = switch (rel) {
                case ">" -> "<";
                case ">=" -> "<=";
                case "<" -> ">";
                default -> ">=";
            };
        }
        return switch (r) {
            case ">" -> "x > " + x0 + "  →  (" + x0 + ", +∞)";
            case ">=" -> "x ≥ " + x0 + "  →  [" + x0 + ", +∞)";
            case "<" -> "x < " + x0 + "  →  (-∞, " + x0 + ")";
            default -> "x ≤ " + x0 + "  →  (-∞, " + x0 + "]";
        };
    }

    static String normalize(String rel) {
        if (rel == null) {
            return "?";
        }
        return rel.trim().replace("≥", ">=").replace("≤", "<=").replace("=>", ">=").replace("=<", "<=");
    }
}
