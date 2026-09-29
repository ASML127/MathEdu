package com.mathedu.domain.calculators.plane;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.units.LengthUnit;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Casos normales de PERIMETRO / longitudes para las 15 figuras. */
class PlanePerimeterTest {

    private static final Offset<Double> EPS = Offset.offset(1e-9);
    private static final LengthUnit CM = LengthUnit.CM;

    @Test
    void cuadradoRectangulo() throws CalcException {
        assertThat(new SquarePerimeterCalculator()
                .calculate(new SquarePerimeterCalculator.Input(4, CM)).value()).isCloseTo(16, EPS);
        assertThat(new RectanglePerimeterCalculator()
                .calculate(new RectanglePerimeterCalculator.Input(5, 3, CM)).value())
                .isCloseTo(16, EPS);
    }

    @Test
    void triangulos() throws CalcException {
        assertThat(new TrianglePerimeterCalculator()
                .calculate(new TrianglePerimeterCalculator.Input(3, 4, 5, CM)).value())
                .isCloseTo(12, EPS);
        assertThat(new EquilateralPerimeterCalculator()
                .calculate(new EquilateralPerimeterCalculator.Input(6, CM)).value())
                .isCloseTo(18, EPS);
        assertThat(new IsoscelesPerimeterCalculator()
                .calculate(new IsoscelesPerimeterCalculator.Input(8, 6, CM)).value())
                .isCloseTo(20, EPS);
        assertThat(new RightPerimeterCalculator()
                .calculate(new RightPerimeterCalculator.Input(3, 4, CM)).value())
                .isCloseTo(12, EPS);
    }

    @Test
    void cuadrilateros() throws CalcException {
        assertThat(new ParallelogramPerimeterCalculator()
                .calculate(new ParallelogramPerimeterCalculator.Input(6, 5, 4, CM)).value())
                .isCloseTo(22, EPS);
        assertThat(new RhombusPerimeterCalculator()
                .calculate(new RhombusPerimeterCalculator.Input(5, CM)).value())
                .isCloseTo(20, EPS);
        assertThat(new TrapezoidPerimeterCalculator()
                .calculate(new TrapezoidPerimeterCalculator.Input(10, 6, 5, 5, CM)).value())
                .isCloseTo(26, EPS);
        assertThat(new KitePerimeterCalculator()
                .calculate(new KitePerimeterCalculator.Input(5, 7, CM)).value())
                .isCloseTo(24, EPS);
    }

    @Test
    void circulares() throws CalcException {
        assertThat(new CirclePerimeterCalculator()
                .calculate(new CirclePerimeterCalculator.Input(3, CM)).value())
                .isCloseTo(6 * Math.PI, EPS);
        assertThat(new SemicirclePerimeterCalculator()
                .calculate(new SemicirclePerimeterCalculator.Input(4, CM)).value())
                .isCloseTo(4 * Math.PI + 8, EPS);
        assertThat(new SectorArcCalculator()
                .calculate(new SectorArcCalculator.Input(5, 90, CM)).value())
                .isCloseTo(5 * Math.PI / 2, EPS);
        assertThat(new AnnulusPerimeterCalculator()
                .calculate(new AnnulusPerimeterCalculator.Input(5, 3, CM)).value())
                .isCloseTo(16 * Math.PI, EPS);
    }

    @Test
    void poligonoRegular() throws CalcException {
        assertThat(new RegularPolygonPerimeterCalculator()
                .calculate(new RegularPolygonPerimeterCalculator.Input(6, 4, CM)).value())
                .isCloseTo(24, EPS);
    }
}
