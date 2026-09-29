package com.mathedu.domain.calculators.analytic;

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

class AnalyticGeometryPhase3Test {

    @Test
    void calculatesDistanceMidpointSlopeAndLineForms() throws CalcException {
        assertThat(new AnalyticGeometryCalculator.Distance().calculate(
                new AnalyticGeometryCalculator.Distance.Input(0, 0, 3, 4)).value()).isEqualTo(5);
        var midpoint = new AnalyticGeometryCalculator.Midpoint().calculate(
                new AnalyticGeometryCalculator.Midpoint.Input(0, 2, 4, 6));
        assertThat(midpoint.value()).isEqualTo(2);
        assertThat(midpoint.steps()).anyMatch(step -> step.contains("M = (2.0, 4.0)"));
        assertThat(new AnalyticGeometryCalculator.Slope().calculate(
                new AnalyticGeometryCalculator.Slope.Input(1, 2, 3, 6)).value()).isEqualTo(2);
        assertThat(new AnalyticGeometryCalculator.LineFromPoints().calculate(
                new AnalyticGeometryCalculator.LineFromPoints.Input(1, 2, 3, 6)).formula())
                .contains("y = 2.0x + 0.0");
        assertThat(new AnalyticGeometryCalculator.PointSlope().calculate(
                new AnalyticGeometryCalculator.PointSlope.Input(1, 2, 2)).formula())
                .isEqualTo("y = 2.0x + 0.0");
        assertThat(new AnalyticGeometryCalculator.LineFromSlopeIntercept().calculate(
                new AnalyticGeometryCalculator.LineFromSlopeIntercept.Input(2, 3)).formula())
                .contains("2x - y + 3 = 0");
        assertThat(new AnalyticGeometryCalculator.GeneralLineConversion().calculate(
                new AnalyticGeometryCalculator.GeneralLineConversion.Input(2, -1, 3)).formula())
                .contains("y = 2.0x + 3.0");
    }

    @Test
    void calculatesLineIntersectionPointDistanceAndCircle() throws CalcException {
        var intersection = new AnalyticGeometryCalculator.LineIntersection().calculate(
                new AnalyticGeometryCalculator.LineIntersection.Input(2, 3, -7, 1, -1, -1));
        assertThat(intersection.value()).isEqualTo(2);
        assertThat(intersection.formula()).contains("(2.0, 1.0)");

        assertThat(new AnalyticGeometryCalculator.PointLineDistance().calculate(
                new AnalyticGeometryCalculator.PointLineDistance.Input(1, 1, 3, 4, -10)).value())
                .isCloseTo(0.6, org.assertj.core.data.Offset.offset(1e-12));

        var circle = new AnalyticGeometryCalculator.CircleEquation().calculate(
                new AnalyticGeometryCalculator.CircleEquation.Input(2, 3, 4));
        assertThat(circle.formula()).isEqualTo("(x - 2.0)² + (y - 3.0)² = 16.0");
    }

    @Test
    void analyticFormulasAreRegisteredAndBuildableFromTheDynamicForm() throws CalcException {
        var registry = AppConfig.buildRegistry();
        var service = new CalculationService(registry);
        var factory = new FormulaInputFactory();
        var input = factory.buildInput(AnalyticGeometryCalculator.Distance.FORMULA_ID,
                Map.of("x1", "0", "y1", "0", "x2", "3", "y2", "4"),
                LengthUnit.CM, AngleUnit.DEG);

        assertThat(service.formulas()).anyMatch(formula ->
                formula.id().equals(AnalyticGeometryCalculator.Distance.FORMULA_ID));
        assertThat(service.compute(AnalyticGeometryCalculator.Distance.FORMULA_ID, input).value())
                .isEqualTo(5);
        assertThat(AppCategory.ANALYTIC.isAvailable()).isTrue();
    }
}