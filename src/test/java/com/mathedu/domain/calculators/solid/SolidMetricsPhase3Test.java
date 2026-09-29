package com.mathedu.domain.calculators.solid;

import com.mathedu.app.AppConfig;
import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolidMetricsPhase3Test {

    @Test
    void computesCubeSphereConeAndPrismMetrics() throws CalcException {
        var cube = calculate(SolidMetricsCalculator.Kind.CUBE,
                new SolidMetricsCalculator.Input(0, 0, 0, 3, 0, 0, 0, 0, LengthUnit.CM));
        assertThat(cube.value()).isEqualTo(27);
        assertThat(cube.steps()).contains("Area lateral: 36.0 cm^2", "Area total: 54.0 cm^2");

        var sphere = calculate(SolidMetricsCalculator.Kind.SPHERE,
                new SolidMetricsCalculator.Input(0, 0, 0, 0, 2, 0, 0, 0, LengthUnit.CM));
        assertThat(sphere.value()).isCloseTo(33.5103216383,
                org.assertj.core.data.Offset.offset(1e-9));

        var cone = calculate(SolidMetricsCalculator.Kind.CONE,
                new SolidMetricsCalculator.Input(0, 0, 4, 0, 3, 0, 0, 0, LengthUnit.CM));
        assertThat(cone.value()).isCloseTo(12 * Math.PI,
                org.assertj.core.data.Offset.offset(1e-12));
        assertThat(cone.steps()).contains("Area lateral: " + (15 * Math.PI) + " cm^2");

        var prism = calculate(SolidMetricsCalculator.Kind.REGULAR_PRISM,
                new SolidMetricsCalculator.Input(0, 0, 3, 2, 0, 0, 0, 4, LengthUnit.CM));
        assertThat(prism.value()).isCloseTo(12, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(prism.steps()).contains("Area lateral: 24.0 cm^2", "Area total: 32.0 cm^2");
    }

    @Test
    void computesCuboidAndFrustumAndValidatesDimensions() throws CalcException {
        var cuboid = calculate(SolidMetricsCalculator.Kind.CUBOID,
                new SolidMetricsCalculator.Input(2, 3, 4, 0, 0, 0, 0, 0, LengthUnit.M));
        assertThat(cuboid.value()).isEqualTo(24);
        assertThat(cuboid.steps()).contains("Area lateral: 40.0 m^2", "Area total: 52.0 m^2");

        var frustum = calculate(SolidMetricsCalculator.Kind.CONE_FRUSTUM,
                new SolidMetricsCalculator.Input(0, 0, 4, 0, 0, 3, 1, 0, LengthUnit.CM));
        assertThat(frustum.value()).isCloseTo(52 * Math.PI / 3,
                org.assertj.core.data.Offset.offset(1e-12));

        assertThatThrownBy(() -> calculate(SolidMetricsCalculator.Kind.CUBE,
                new SolidMetricsCalculator.Input(0, 0, 0, -1, 0, 0, 0, 0, LengthUnit.CM)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void allTenSolidsAreRegisteredAndReachableFromTheForm() throws CalcException {
        var service = new CalculationService(AppConfig.buildRegistry());
        var formulas = service.formulas().stream()
                .filter(formula -> formula.category().equals("Geometría 3D")).toList();
        assertThat(formulas).hasSize(10);
        assertThat(AppCategory.SOLID.isAvailable()).isTrue();

        var input = new FormulaInputFactory().buildInput("solid.sphere",
                Map.of("radius", "2"), LengthUnit.CM, AngleUnit.DEG);
        assertThat(service.compute("solid.sphere", input).value())
                .isCloseTo(33.5103216383, org.assertj.core.data.Offset.offset(1e-9));
    }

    private static com.mathedu.shared.result.CalculationResult calculate(
            SolidMetricsCalculator.Kind kind, SolidMetricsCalculator.Input input) throws CalcException {
        return new SolidMetricsCalculator(kind).calculate(input);
    }
}