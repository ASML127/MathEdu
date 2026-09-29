package com.mathedu.presentation.navigation;

/** Categorias del menu lateral. Solo las disponibles tienen formulas registradas. */
public enum AppCategory {
    PLANE("Geometría plana", true),
    ALGEBRA("Aritmética y álgebra", true),
    TRIG("Trigonometría", true),
    ANALYTIC("Geometría analítica", true),
    SOLID("Geometría 3D", true),
    DIFF("Cálculo diferencial", true),
    INTEGRAL("Cálculo integral", true),
    SERIES("Series y sucesiones", true),
    STATS("Estadística y probabilidad", true),
    LINEAR("Álgebra lineal", true);

    private final String displayName;
    private final boolean available;

    AppCategory(String displayName, boolean available) {
        this.displayName = displayName;
        this.available = available;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return displayName + (available ? "" : " (próximamente)");
    }
}
