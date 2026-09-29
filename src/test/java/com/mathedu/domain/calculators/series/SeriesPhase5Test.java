package com.mathedu.domain.calculators.series;

import com.mathedu.app.AppConfig;
import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.infrastructure.engine.EvalExEngine;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeriesPhase5Test {

    @Test
    void calculatesArithmeticAndGeometricSequenceTermsAndPartialSums() throws CalcException {
        var arithmetic = new SeriesCalculator.ArithmeticSequence().calculate(
                new SeriesCalculator.ArithmeticInput(2, 3, 4));
        assertThat(arithmetic.value()).isEqualTo(11);
        assertThat(arithmetic.steps()).anyMatch(step -> step.endsWith("= 26.0"));

        var geometric = new SeriesCalculator.GeometricSequence().calculate(
                new SeriesCalculator.GeometricInput(2, 0.5, 4));
        assertThat(geometric.value()).isCloseTo(0.25,
                org.assertj.core.data.Offset.offset(1e-12));
        assertThat(geometric.steps()).anyMatch(step -> step.contains("Sₙ = 3.75"));
    }

    @Test
    void geometricSeriesAndFiniteSumRespectConvergenceAndLimits() throws CalcException {
        assertThat(new SeriesCalculator.InfiniteGeometric().calculate(
                new SeriesCalculator.InfiniteGeometricInput(2, 0.5)).value()).isEqualTo(4);
        assertThatThrownBy(() -> new SeriesCalculator.InfiniteGeometric().calculate(
                new SeriesCalculator.InfiniteGeometricInput(2, 1)))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);

        var finite = new SeriesCalculator.FiniteSum(new EvalExEngine()).calculate(
                new SeriesCalculator.FiniteSumInput("1/n^2", 1, 3));
        assertThat(finite.value()).isCloseTo(1.3611111111,
                org.assertj.core.data.Offset.offset(1e-10));
        assertThatThrownBy(() -> new SeriesCalculator.FiniteSum(new EvalExEngine()).calculate(
                new SeriesCalculator.FiniteSumInput("n", Integer.MIN_VALUE, Integer.MAX_VALUE)))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
    }

    @Test
    void ratioRootAndComparisonTestsDistinguishConvergenceDivergenceAndUnknown() throws CalcException {
        assertThat(new SeriesCalculator.RatioTest().calculate(
                new SeriesCalculator.CriterionInput(0.5)).steps())
                .anyMatch(step -> step.contains("converge absolutamente"));
        assertThat(new SeriesCalculator.RootTest().calculate(
                new SeriesCalculator.CriterionInput(1)).steps())
                .anyMatch(step -> step.contains("inconcluso"));
        assertThat(new SeriesCalculator.RatioTest().calculate(
                new SeriesCalculator.CriterionInput(2)).steps())
                .anyMatch(step -> step.contains("diverge"));

        var comparison = new SeriesCalculator.LimitComparison().calculate(
                new SeriesCalculator.ComparisonInput(2, "mayorante-convergente"));
        assertThat(comparison.steps()).anyMatch(step -> step.contains("la serie de aₙ converge"));
        assertThatThrownBy(() -> new SeriesCalculator.LimitComparison().calculate(
                new SeriesCalculator.ComparisonInput(0, "minorante-divergente")))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void phaseFiveFormulasAreRegisteredAndAvailableThroughDynamicInputs() throws CalcException {
        var registry = AppConfig.buildRegistry();
        var service = new CalculationService(registry);
        var formulas = service.formulas().stream()
                .filter(formula -> formula.category().equals("Series y sucesiones")).toList();
        assertThat(formulas).hasSize(8);
        assertThat(service.formulas().stream()
                .filter(formula -> formula.category().equals("Cálculo diferencial"))).hasSize(1);
        assertThat(service.formulas().stream()
                .filter(formula -> formula.category().equals("Cálculo integral"))).hasSize(2);
        assertThat(AppCategory.DIFF.isAvailable()).isTrue();
        assertThat(AppCategory.INTEGRAL.isAvailable()).isTrue();
        assertThat(AppCategory.SERIES.isAvailable()).isTrue();

        var input = new FormulaInputFactory().buildInput(SeriesCalculator.ArithmeticSequence.FORMULA_ID,
                Map.of("first", "2", "difference", "3", "terms", "4"),
                LengthUnit.CM, AngleUnit.DEG);
        assertThat(service.compute(SeriesCalculator.ArithmeticSequence.FORMULA_ID, input).value())
                .isEqualTo(11);
    }

        @Test
        void computesSimpleLimitWithSymbolicEngine() throws CalcException {
                var result = new SeriesCalculator.SimpleLimit(new SymbolicStub()).calculate(
                                new SeriesCalculator.LimitInput("(x^2-1)/(x-1)", "x", 1));
                assertThat(result.value()).isEqualTo(2);
        }

        private static final class SymbolicStub implements com.mathedu.domain.engine.SymbolicEngine {
                @Override public String simplify(String expression) { return expression; }
                @Override public String differentiate(String expression, String variable, int order) {
                        return expression;
                }
                @Override public String integrate(String expression, String variable) { return expression; }
                @Override public double integrateDefinite(String expression, String variable,
                                                                                                  double lower, double upper) { return 0; }
                @Override public double limit(String expression, String variable, double point) { return 2; }
        }
}