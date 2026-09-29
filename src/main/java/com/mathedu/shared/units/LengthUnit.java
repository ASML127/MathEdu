package com.mathedu.shared.units;

/**
 * Unidades de longitud. El dominio calcula en la unidad que recibe;
 * la conversion se hace solo en presentacion (Fase 1: selector completo).
 */
public enum LengthUnit {
    MM("mm", 0.001),
    CM("cm", 0.01),
    M("m", 1.0),
    KM("km", 1000.0),
    IN("in", 0.0254),
    FT("ft", 0.3048);

    private final String symbol;
    private final double toMeters;

    LengthUnit(String symbol, double toMeters) {
        this.symbol = symbol;
        this.toMeters = toMeters;
    }

    public String getSymbol() {
        return symbol;
    }

    /** Convierte un valor en esta unidad a metros. */
    public double toMeters(double value) {
        return value * toMeters;
    }

    /** Convierte un valor en metros a esta unidad. */
    public double fromMeters(double meters) {
        return meters / toMeters;
    }

    /** Convierte longitudes lineales entre unidades. */
    public static double convert(double value, LengthUnit from, LengthUnit to) {
        return to.fromMeters(from.toMeters(value));
    }

    /** Simbolo de area (cm^2) / volumen (cm^3) segun dimension. */
    public String areaSymbol() {
        return symbol + "^2";
    }

    public String volumeSymbol() {
        return symbol + "^3";
    }
}
