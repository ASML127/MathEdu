package com.mathedu.domain.calculators.plane;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Casos invalidos: validacion, dominio, overflow (Fase 1). */
class PlaneValidationTest {

    private static final LengthUnit CM = LengthUnit.CM;

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, -0.5, Double.NaN, Double.POSITIVE_INFINITY})
    void ladosNoPositivosSonRechazados(double bad) {
        assertThatThrownBy(() -> new SquareAreaCalculator()
                .calculate(new SquareAreaCalculator.Input(bad, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isIn(ErrorCode.INVALID_RANGE, ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> new RectangleAreaCalculator()
                .calculate(new RectangleAreaCalculator.Input(bad, 3, CM)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void desigualdadTriangular() {
        // 1 + 2 <= 10: no es triangulo
        assertThatThrownBy(() -> new TriangleHeronCalculator()
                .calculate(new TriangleHeronCalculator.Input(1, 2, 10, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);
        assertThatThrownBy(() -> new TrianglePerimeterCalculator()
                .calculate(new TrianglePerimeterCalculator.Input(1, 2, 10, CM)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void isoscelesInvalido() {
        // base 20 con lados 6: 2*6 <= 20
        assertThatThrownBy(() -> new IsoscelesPerimeterCalculator()
                .calculate(new IsoscelesPerimeterCalculator.Input(20, 6, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);
    }

    @Test
    void coronaExigeExteriorMayor() {
        assertThatThrownBy(() -> new AnnulusAreaCalculator()
                .calculate(new AnnulusAreaCalculator.Input(3, 5, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
        assertThatThrownBy(() -> new AnnulusAreaCalculator()
                .calculate(new AnnulusAreaCalculator.Input(4, 4, CM)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void anguloFueraDeRango() {
        assertThatThrownBy(() -> new SectorAreaCalculator()
                .calculate(new SectorAreaCalculator.Input(5, 0, CM)))
                .isInstanceOf(CalcException.class);
        assertThatThrownBy(() -> new SectorAreaCalculator()
                .calculate(new SectorAreaCalculator.Input(5, 400, CM)))
                .isInstanceOf(CalcException.class);
        assertThatThrownBy(() -> new SectorArcCalculator()
                .calculate(new SectorArcCalculator.Input(5, -30, CM)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void poligonoMinimo3Lados() {
        assertThatThrownBy(() -> new RegularPolygonAreaCalculator()
                .calculate(new RegularPolygonAreaCalculator.Input(2, 4, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
    }

    @Test
    void paralelogramoAlturaCoherente() {
        // altura mayor que el lado oblicuo es geometricamente imposible
        assertThatThrownBy(() -> new ParallelogramPerimeterCalculator()
                .calculate(new ParallelogramPerimeterCalculator.Input(6, 3, 5, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);
    }

    @Test
    void overflowEnMagnitudesAbsurdas() {
        assertThatThrownBy(() -> new SquareAreaCalculator()
                .calculate(new SquareAreaCalculator.Input(2e9, CM)))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.OVERFLOW);
    }

    @Test
    void inputNulo() {
        assertThatThrownBy(() -> new SquareAreaCalculator().calculate(null))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }
}
