package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.shapes.plane.Rhombus;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.LengthUnit;

import java.util.List;

/** Area del rombo por diagonales: A = D*d/2. */
public class RhombusAreaCalculator implements Calculator<RhombusAreaCalculator.Input> {

    public static final String FORMULA_ID = "plane.rhombus.area";

    public record Input(double diagMajor, double diagMinor, LengthUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Rombo — Area (diagonales)",
            "Geometria plana",
            "A = D * d / 2",
            List.of(
                    InputDef.positive("diagMajor", "Diagonal mayor (D)", "Ej. 10"),
                    InputDef.positive("diagMinor", "Diagonal menor (d)", "Ej. 6")),
            "Area",
            "unidad^2");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double d1 = Guards.requirePositive(in.diagMajor(), "diagMajor", "Diagonal mayor");
        double d2 = Guards.requirePositive(in.diagMinor(), "diagMinor", "Diagonal menor");
        LengthUnit unit = in.unit() == null ? LengthUnit.CM : in.unit();
        double a = Guards.requireFiniteResult(Rhombus.area(d1, d2));
        List<String> steps = List.of(
                "Formula: A = D * d / 2",
                "Sustitucion: A = " + d1 + " * " + d2 + " / 2",
                "Resultado: A = " + a + " " + unit.areaSymbol());
        return new CalculationResult(a, "Area", "A = D * d / 2", steps,
                unit.areaSymbol(), true, null);
    }
}
