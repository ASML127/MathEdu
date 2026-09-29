package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.calculators.Calculator;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.validation.Guards;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.AngleUnit;

import java.util.List;

/** Arcocoseno: devuelve el angulo en la unidad seleccionada (°/rad). */
public class AcosCalculator implements Calculator<AcosCalculator.Input> {

    public static final String FORMULA_ID = "trig.acos";

    public record Input(double value, AngleUnit unit) {
    }

    private static final FormulaDef DEF = new FormulaDef(
            FORMULA_ID,
            "Arcocoseno",
            "Trigonometría",
            "θ = arccos(x),  x ∈ [-1, 1]",
            List.of(new InputDef("x", "Valor (x)", "Ej. 0.5", 0, 0, true)),
            "θ = arccos(x)",
            "°/rad");

    @Override
    public FormulaDef formula() {
        return DEF;
    }

    @Override
    public CalculationResult calculate(Input in) throws CalcException {
        if (in == null) {
            throw new CalcException(ErrorCode.INVALID_INPUT, "input", "Datos de entrada vacios.");
        }
        double x = Guards.requireFinite(in.value(), "x", "Valor");
        if (x < -1 || x > 1) {
            throw new CalcException(ErrorCode.DOMAIN_ERROR, "x",
                    "arccos solo esta definido para x en [-1, 1]. Recibido: " + x);
        }
        AngleUnit au = in.unit() == null ? AngleUnit.DEG : in.unit();
        double deg = Math.toDegrees(Math.acos(x));
        double out = Guards.requireFiniteResult(au.fromDegrees(deg));
        List<String> steps = List.of(
                "Funcion inversa: θ = arccos(" + x + ")",
                "En grados: θ = " + deg + "°",
                "Resultado: θ = " + out + " " + au.getSymbol());
        return new CalculationResult(out, "θ = arccos(x)", "θ = arccos(x)",
                steps, au.getSymbol(), true, null);
    }
}
