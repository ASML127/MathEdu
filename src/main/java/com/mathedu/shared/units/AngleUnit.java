package com.mathedu.shared.units;

/** Unidades de angulo con conversion. */
public enum AngleUnit {
    DEG("°", "grados"),
    RAD("rad", "radianes");

    private final String symbol;
    private final String displayName;

    AngleUnit(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public double toDegrees(double value) {
        return this == DEG ? value : Math.toDegrees(value);
    }

    public double fromDegrees(double degrees) {
        return this == DEG ? degrees : Math.toRadians(degrees);
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }
}
