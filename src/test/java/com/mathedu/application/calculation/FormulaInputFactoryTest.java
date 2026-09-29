package com.mathedu.application.calculation;

import com.mathedu.domain.calculators.geometry.CircleAreaCalculator;
import com.mathedu.domain.calculators.algebra.LinearEquationCalculator;
import com.mathedu.domain.calculators.plane.RegularPolygonAreaCalculator;
import com.mathedu.domain.calculators.plane.SectorAreaCalculator;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Parseo de texto del formulario dinamico a inputs tipados. */
class FormulaInputFactoryTest {

    private final FormulaInputFactory factory = new FormulaInputFactory();

    @Test
    void aceptaComaDecimal() throws CalcException {
        Object in = factory.buildInput(CircleAreaCalculator.FORMULA_ID,
                Map.of("r", "3,5"), LengthUnit.CM, AngleUnit.DEG);
        assertThat(in).isInstanceOfSatisfying(
                com.mathedu.domain.calculators.geometry.CircleAreaInput.class,
                c -> assertThat(c.radius()).isEqualTo(3.5));
    }

    @Test
    void convierteRadianesAGrados() throws CalcException {
        Object in = factory.buildInput(SectorAreaCalculator.FORMULA_ID,
                Map.of("r", "5", "angle", String.valueOf(Math.PI / 2)),
                LengthUnit.CM, AngleUnit.RAD);
        assertThat(in).isInstanceOfSatisfying(SectorAreaCalculator.Input.class,
                s -> assertThat(s.angleDeg()).isCloseTo(90.0,
                        org.assertj.core.data.Offset.offset(1e-9)));
    }

    @Test
    void gradosSinConversion() throws CalcException {
        Object in = factory.buildInput(SectorAreaCalculator.FORMULA_ID,
                Map.of("r", "5", "angle", "90"), LengthUnit.M, AngleUnit.DEG);
        assertThat(in).isInstanceOfSatisfying(SectorAreaCalculator.Input.class,
                s -> assertThat(s.angleDeg()).isEqualTo(90.0));
    }

    @Test
    void ladosEnterosDelPoligono() throws CalcException {
        Object in = factory.buildInput(RegularPolygonAreaCalculator.FORMULA_ID,
                Map.of("n", "6", "side", "4"), LengthUnit.CM, AngleUnit.DEG);
        assertThat(in).isInstanceOfSatisfying(RegularPolygonAreaCalculator.Input.class,
                p -> assertThat(p.sides()).isEqualTo(6));
    }

    @Test
    void ladosNoEnterosRechazados() {
        assertThatThrownBy(() -> factory.buildInput(RegularPolygonAreaCalculator.FORMULA_ID,
                Map.of("n", "6.5", "side", "4"), LengthUnit.CM, AngleUnit.DEG))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void textoNoNumericoIndicaCampo() {
        assertThatThrownBy(() -> factory.buildInput(CircleAreaCalculator.FORMULA_ID,
                Map.of("r", "abc"), LengthUnit.CM, AngleUnit.DEG))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getField())
                .isEqualTo("r");
    }

    @Test
    void campoVacioIndicaCampo() {
        assertThatThrownBy(() -> factory.buildInput(CircleAreaCalculator.FORMULA_ID,
                Map.of("r", "  "), LengthUnit.CM, AngleUnit.DEG))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void construyeEcuacionLinealConSegundoMiembro() throws CalcException {
        Object in = factory.buildInput(LinearEquationCalculator.FORMULA_ID,
                Map.of("a", "2", "b", "3", "c", "7"), LengthUnit.CM, AngleUnit.DEG);
        assertThat(in).isInstanceOf(LinearEquationCalculator.Input.class);
        var result = new LinearEquationCalculator().calculate(
                (LinearEquationCalculator.Input) in);
        assertThat(result.value()).isEqualTo(2.0);
    }

    @Test
    void formulaDesconocida() {
        assertThatThrownBy(() -> factory.buildInput("no.existe",
                Map.of(), LengthUnit.CM, AngleUnit.DEG))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.UNKNOWN_FORMULA);
    }
}
