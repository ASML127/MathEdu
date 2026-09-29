package com.mathedu.domain.calculators.plane;

import com.mathedu.domain.calculators.geometry.CircleAreaCalculator;
import com.mathedu.domain.calculators.geometry.CircleAreaInput;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.units.LengthUnit;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Casos normales de AREA para las 15 figuras (valores de referencia). */
class PlaneAreaTest {

    private static final Offset<Double> EPS = Offset.offset(1e-9);
    private static final LengthUnit CM = LengthUnit.CM;

    @Test
    void cuadradoRectangulo() throws CalcException {
        assertThat(new SquareAreaCalculator()
                .calculate(new SquareAreaCalculator.Input(4, CM)).value()).isCloseTo(16, EPS);
        assertThat(new RectangleAreaCalculator()
                .calculate(new RectangleAreaCalculator.Input(5, 3, CM)).value()).isCloseTo(15, EPS);
    }

    @Test
    void triangulos() throws CalcException {
        assertThat(new TriangleBaseHeightCalculator()
                .calculate(new TriangleBaseHeightCalculator.Input(6, 4, CM)).value())
                .isCloseTo(12, EPS);
        assertThat(new TriangleHeronCalculator()
                .calculate(new TriangleHeronCalculator.Input(3, 4, 5, CM)).value())
                .isCloseTo(6, EPS);
        assertThat(new EquilateralAreaCalculator()
                .calculate(new EquilateralAreaCalculator.Input(6, CM)).value())
                .isCloseTo(9 * Math.sqrt(3), Offset.offset(1e-9));
        assertThat(new IsoscelesAreaCalculator()
                .calculate(new IsoscelesAreaCalculator.Input(8, 5, CM)).value())
                .isCloseTo(20, EPS);
        assertThat(new RightAreaCalculator()
                .calculate(new RightAreaCalculator.Input(3, 4, CM)).value())
                .isCloseTo(6, EPS);
    }

    @Test
    void cuadrilateros() throws CalcException {
        assertThat(new ParallelogramAreaCalculator()
                .calculate(new ParallelogramAreaCalculator.Input(6, 4, CM)).value())
                .isCloseTo(24, EPS);
        assertThat(new RhombusAreaCalculator()
                .calculate(new RhombusAreaCalculator.Input(10, 6, CM)).value())
                .isCloseTo(30, EPS);
        assertThat(new TrapezoidAreaCalculator()
                .calculate(new TrapezoidAreaCalculator.Input(10, 6, 4, CM)).value())
                .isCloseTo(32, EPS);
        assertThat(new KiteAreaCalculator()
                .calculate(new KiteAreaCalculator.Input(12, 8, CM)).value())
                .isCloseTo(48, EPS);
    }

    @Test
    void circulares() throws CalcException {
        assertThat(new CircleAreaCalculator()
                .calculate(new CircleAreaInput(3, CM)).value())
                .isCloseTo(Math.PI * 9, EPS);
        assertThat(new SemicircleAreaCalculator()
                .calculate(new SemicircleAreaCalculator.Input(4, CM)).value())
                .isCloseTo(8 * Math.PI, EPS);
        assertThat(new SectorAreaCalculator()
                .calculate(new SectorAreaCalculator.Input(5, 90, CM)).value())
                .isCloseTo(25 * Math.PI / 4, EPS);
        assertThat(new AnnulusAreaCalculator()
                .calculate(new AnnulusAreaCalculator.Input(5, 3, CM)).value())
                .isCloseTo(16 * Math.PI, EPS);
    }

    @Test
    void poligonoRegularHexagono() throws CalcException {
        // Hexagono lado 4: A = 6*16/(4*tan(pi/6)) ≈ 41.5692
        assertThat(new RegularPolygonAreaCalculator()
                .calculate(new RegularPolygonAreaCalculator.Input(6, 4, CM)).value())
                .isCloseTo(41.569219381653056, Offset.offset(1e-6));
    }

    @Test
    void resultadosLlevanFormulaPasosYUnidad() throws CalcException {
        var r = new SquareAreaCalculator()
                .calculate(new SquareAreaCalculator.Input(4, LengthUnit.M));
        assertThat(r.formula()).contains("l^2");
        assertThat(r.steps()).hasSize(3);
        assertThat(r.unitSymbol()).isEqualTo("m^2");
        assertThat(r.exact()).isTrue();
    }
}
