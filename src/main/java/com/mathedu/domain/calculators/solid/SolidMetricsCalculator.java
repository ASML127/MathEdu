package com.mathedu.domain.calculators.solid;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.ArrayList;
import java.util.List;

/** Calcula areas y volumen de diez solidos habituales. */
public final class SolidMetricsCalculator implements Calculator<SolidMetricsCalculator.Input> {

    public enum Kind {
        CUBE("cube", "Cubo", "Lado", "side"),
        CUBOID("cuboid", "Prisma rectangular", "Largo, ancho y alto", "length", "width", "height"),
        REGULAR_PRISM("regular-prism", "Prisma regular", "Numero de lados, lado y altura", "n", "side", "height"),
        REGULAR_PYRAMID("regular-pyramid", "Piramide regular", "Numero de lados, lado y altura", "n", "side", "height"),
        CYLINDER("cylinder", "Cilindro", "Radio y altura", "radius", "height"),
        CONE("cone", "Cono", "Radio y altura", "radius", "height"),
        CONE_FRUSTUM("cone-frustum", "Tronco de cono", "Radios mayor y menor, y altura", "outerRadius", "innerRadius", "height"),
        SPHERE("sphere", "Esfera", "Radio", "radius"),
        HEMISPHERE("hemisphere", "Hemisferio", "Radio", "radius"),
        TETRAHEDRON("tetrahedron", "Tetraedro regular", "Lado", "side");

        private final String slug;
        private final String label;
        private final String dimensions;
        private final List<String> keys;

        Kind(String slug, String label, String dimensions, String... keys) {
            this.slug = slug;
            this.label = label;
            this.dimensions = dimensions;
            this.keys = List.of(keys);
        }

        public String formulaId() {
            return "solid." + slug;
        }
    }

    public static Kind findKind(String formulaId) {
        for (Kind kind : Kind.values()) {
            if (kind.formulaId().equals(formulaId)) {
                return kind;
            }
        }
        return null;
    }

    public record Input(double length, double width, double height, double side,
                        double radius, double outerRadius, double innerRadius,
                        int sides, LengthUnit unit) {
    }

    private final Kind kind;
    private final FormulaDef formula;

    public SolidMetricsCalculator(Kind kind) {
        this.kind = java.util.Objects.requireNonNull(kind, "kind");
        this.formula = createFormula(kind);
    }

    @Override
    public FormulaDef formula() {
        return formula;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double lateral;
        double total;
        double volume;
        switch (kind) {
            case CUBE -> {
                double side = positive(in.side(), "side", "Lado");
                lateral = 4 * side * side;
                total = 6 * side * side;
                volume = side * side * side;
            }
            case CUBOID -> {
                double length = positive(in.length(), "length", "Largo");
                double width = positive(in.width(), "width", "Ancho");
                double height = positive(in.height(), "height", "Alto");
                lateral = 2 * height * (length + width);
                total = 2 * (length * width + length * height + width * height);
                volume = length * width * height;
            }
            case REGULAR_PRISM -> {
                double side = positive(in.side(), "side", "Lado de la base");
                double height = positive(in.height(), "height", "Altura");
                int n = requirePolygonSides(in.sides());
                double perimeter = n * side;
                double apothem = side / (2 * Math.tan(Math.PI / n));
                double base = perimeter * apothem / 2;
                lateral = perimeter * height;
                total = lateral + 2 * base;
                volume = base * height;
            }
            case REGULAR_PYRAMID -> {
                double side = positive(in.side(), "side", "Lado de la base");
                double height = positive(in.height(), "height", "Altura");
                int n = requirePolygonSides(in.sides());
                double perimeter = n * side;
                double apothem = side / (2 * Math.tan(Math.PI / n));
                double base = perimeter * apothem / 2;
                double slantHeight = Math.hypot(height, apothem);
                lateral = perimeter * slantHeight / 2;
                total = lateral + base;
                volume = base * height / 3;
            }
            case CYLINDER -> {
                double radius = positive(in.radius(), "radius", "Radio");
                double height = positive(in.height(), "height", "Altura");
                lateral = 2 * Math.PI * radius * height;
                total = lateral + 2 * Math.PI * radius * radius;
                volume = Math.PI * radius * radius * height;
            }
            case CONE -> {
                double radius = positive(in.radius(), "radius", "Radio");
                double height = positive(in.height(), "height", "Altura");
                double slantHeight = Math.hypot(radius, height);
                lateral = Math.PI * radius * slantHeight;
                total = lateral + Math.PI * radius * radius;
                volume = Math.PI * radius * radius * height / 3;
            }
            case CONE_FRUSTUM -> {
                double outer = positive(in.outerRadius(), "outerRadius", "Radio mayor");
                double inner = positive(in.innerRadius(), "innerRadius", "Radio menor");
                double height = positive(in.height(), "height", "Altura");
                if (outer <= inner) {
                    throw new CalcException(ErrorCode.INVALID_RANGE, "outerRadius",
                            "El radio mayor debe superar al radio menor.");
                }
                double slantHeight = Math.hypot(height, outer - inner);
                lateral = Math.PI * (outer + inner) * slantHeight;
                total = lateral + Math.PI * (outer * outer + inner * inner);
                volume = Math.PI * height * (outer * outer + outer * inner + inner * inner) / 3;
            }
            case SPHERE -> {
                double radius = positive(in.radius(), "radius", "Radio");
                lateral = total = 4 * Math.PI * radius * radius;
                volume = 4 * Math.PI * radius * radius * radius / 3;
            }
            case HEMISPHERE -> {
                double radius = positive(in.radius(), "radius", "Radio");
                lateral = 2 * Math.PI * radius * radius;
                total = 3 * Math.PI * radius * radius;
                volume = 2 * Math.PI * radius * radius * radius / 3;
            }
            case TETRAHEDRON -> {
                double side = positive(in.side(), "side", "Lado");
                lateral = 3 * Math.sqrt(3) * side * side / 4;
                total = Math.sqrt(3) * side * side;
                volume = side * side * side / (6 * Math.sqrt(2));
            }
            default -> throw new IllegalStateException("Solid not handled: " + kind);
        }
        lateral = Guards.requireFiniteResult(lateral);
        total = Guards.requireFiniteResult(total);
        volume = Guards.requireFiniteResult(volume);
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        List<String> steps = List.of(
                "Solido: " + kind.label,
                "Area lateral: " + lateral + " " + unit.areaSymbol(),
                "Area total: " + total + " " + unit.areaSymbol(),
                "Volumen: " + volume + " " + unit.volumeSymbol());
        return new CalculationResult(volume, "Volumen", formula.expressionTemplate(), steps,
                unit.volumeSymbol(), true, null);
    }

    private static FormulaDef createFormula(Kind kind) {
        List<InputDef> inputs = new ArrayList<>();
        for (String key : kind.keys) {
            inputs.add(switch (key) {
                case "n" -> new InputDef("n", "Numero de lados n", "Ej. 6", 3, 10000, true);
                case "side" -> InputDef.positive("side", "Lado", "Ej. 4");
                case "length" -> InputDef.positive("length", "Largo", "Ej. 5");
                case "width" -> InputDef.positive("width", "Ancho", "Ej. 3");
                case "height" -> InputDef.positive("height", "Altura", "Ej. 8");
                case "radius" -> InputDef.positive("radius", "Radio", "Ej. 3");
                case "outerRadius" -> InputDef.positive("outerRadius", "Radio mayor", "Ej. 5");
                case "innerRadius" -> InputDef.positive("innerRadius", "Radio menor", "Ej. 2");
                default -> throw new IllegalArgumentException("Unknown dimension: " + key);
            });
        }
        return new FormulaDef(kind.formulaId(), kind.label + " — métricas", "Geometría 3D",
            "Dimensiones: " + kind.dimensions + "; áreas y volumen", inputs,
            "Volumen", "unidad³");
    }

    private static double positive(double value, String field, String label) throws CalcException {
        return Guards.requirePositive(value, field, label);
    }

    private static int requirePolygonSides(int n) throws CalcException {
        if (n < 3 || n > 10000) {
            throw new CalcException(ErrorCode.INVALID_RANGE, "n",
                    "El numero de lados debe estar entre 3 y 10000.");
        }
        return n;
    }
}