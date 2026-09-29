package com.mathedu.domain.trig;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;

/**
 * Resolucion de triangulos cualesquiera (angulos internos en grados).
 * SSS por teorema del coseno; SAS por coseno + senos; ASA por senos.
 */
public final class TriangleSolver {

    private TriangleSolver() {
    }

    /** Lados a,b,c y angulos opuestos A,B,C (grados). */
    public record Solution(double a, double b, double c, double A, double B, double C) {
    }

    /** Tres lados conocidos: halla los tres angulos. */
    public static Solution sss(double a, double b, double c) throws CalcException {
        requireSides(a, b, c);
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "a",
                    "Esos lados no forman un triangulo (desigualdad triangular).");
        }
        double A = Math.toDegrees(Math.acos(clamp((b * b + c * c - a * a) / (2 * b * c))));
        double B = Math.toDegrees(Math.acos(clamp((a * a + c * c - b * b) / (2 * a * c))));
        double C = 180 - A - B;
        return new Solution(a, b, c, A, B, C);
    }

    /** Dos lados a,b y el angulo comprendido C (grados): halla c, A, B. */
    public static Solution sas(double a, double b, double angleCDeg) throws CalcException {
        requireSides(a, b, 1);
        requireAngle(angleCDeg, "angleC");
        double c = Math.sqrt(a * a + b * b - 2 * a * b * Math.cos(Math.toRadians(angleCDeg)));
        double A = Math.toDegrees(Math.asin(clamp(a * Math.sin(Math.toRadians(angleCDeg)) / c)));
        double B = 180 - A - angleCDeg;
        return new Solution(a, b, c, A, B, angleCDeg);
    }

    /** Dos angulos A,B (grados) y el lado comprendido c: halla C, a, b. */
    public static Solution asa(double angleADeg, double angleBDeg, double c) throws CalcException {
        requireAngle(angleADeg, "angleA");
        requireAngle(angleBDeg, "angleB");
        if (angleADeg + angleBDeg >= 180) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "angleA",
                    "A + B debe ser menor que 180°. Recibido: " + (angleADeg + angleBDeg));
        }
        requireSides(c, 1, 1);
        double C = 180 - angleADeg - angleBDeg;
        double sinC = Math.sin(Math.toRadians(C));
        double a = c * Math.sin(Math.toRadians(angleADeg)) / sinC;
        double b = c * Math.sin(Math.toRadians(angleBDeg)) / sinC;
        return new Solution(a, b, c, angleADeg, angleBDeg, C);
    }

    private static void requireSides(double... sides) throws CalcException {
        for (double s : sides) {
            if (Double.isNaN(s) || Double.isInfinite(s) || s <= 0) {
                throw new CalcException(ErrorCode.INVALID_RANGE, "a",
                        "Los lados deben ser mayores que cero.");
            }
            if (s > 1e9) {
                throw new CalcException(ErrorCode.OVERFLOW, "a",
                        "Lado demasiado grande (max 1e9).");
            }
        }
    }

    private static void requireAngle(double deg, String field) throws CalcException {
        if (Double.isNaN(deg) || Double.isInfinite(deg) || deg <= 0 || deg >= 180) {
            throw new CalcException(ErrorCode.INVALID_RANGE, field,
                    "El angulo debe estar en (0, 180) grados. Recibido: " + deg);
        }
    }

    private static double clamp(double v) {
        return Math.max(-1, Math.min(1, v));
    }
}
