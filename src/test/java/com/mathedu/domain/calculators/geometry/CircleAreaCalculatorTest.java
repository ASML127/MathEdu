package com.mathedu.domain.calculators.geometry;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CircleAreaCalculatorTest {

    private final CircleAreaCalculator calc = new CircleAreaCalculator();

    @ParameterizedTest(name = "r={0} -> area={1}")
    @CsvSource({
            "1, 3.141592653589793",
            "3, 28.274333882308138",
            "2.5, 19.634954084936208"
    })
    void areaCasosNormales(double r, double esperado) throws CalcException {
        var result = calc.calculate(new CircleAreaInput(r, LengthUnit.CM));
        assertThat(result.value()).isCloseTo(esperado, org.assertj.core.data.Offset.offset(1e-9));
        assertThat(result.formula()).contains("pi");
        assertThat(result.steps()).hasSize(3);
        assertThat(result.unitSymbol()).isEqualTo("cm^2");
        assertThat(result.exact()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, -0.001, Double.NaN, Double.POSITIVE_INFINITY})
    void areaCasosInvalidos(double r) {
        assertThatThrownBy(() -> calc.calculate(new CircleAreaInput(r, LengthUnit.CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isIn(ErrorCode.INVALID_RANGE, ErrorCode.INVALID_INPUT);
    }

    @Test
    void radioGiganteDaOverflow() {
        assertThatThrownBy(() -> calc.calculate(new CircleAreaInput(2e9, LengthUnit.M)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.OVERFLOW);
    }

    @Test
    void formulaDefExponeMetadatosParaUIDinamica() {
        var def = calc.formula();
        assertThat(def.id()).isEqualTo(CircleAreaCalculator.FORMULA_ID);
        assertThat(def.inputs()).hasSize(1);
        assertThat(def.inputs().get(0).key()).isEqualTo("r");
    }
}
