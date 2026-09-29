package com.mathedu.domain.algebra;

/**
 * Fraccion exacta con signo normalizado (denominador positivo) y
 * simplificacion por MCD. Inmutable.
 */
public record Fraction(long numerator, long denominator) {

    public Fraction {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominador cero.");
        }
        long g = gcd(Math.abs(numerator), Math.abs(denominator));
        long n = numerator / g;
        long d = denominator / g;
        if (d < 0) {
            n = -n;
            d = -d;
        }
        numerator = n;
        denominator = d;
    }

    public static Fraction of(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }

    public Fraction add(Fraction other) {
        return new Fraction(
                numerator * other.denominator + other.numerator * denominator,
                denominator * other.denominator);
    }

    public Fraction subtract(Fraction other) {
        return new Fraction(
                numerator * other.denominator - other.numerator * denominator,
                denominator * other.denominator);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(numerator * other.numerator, denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new IllegalArgumentException("Division entre cero.");
        }
        return new Fraction(numerator * other.denominator, denominator * other.numerator);
    }

    public double doubleValue() {
        return (double) numerator / denominator;
    }

    public boolean isInteger() {
        return denominator == 1;
    }

    @Override
    public String toString() {
        return isInteger() ? Long.toString(numerator) : numerator + "/" + denominator;
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a == 0 ? 1 : a;
    }
}
