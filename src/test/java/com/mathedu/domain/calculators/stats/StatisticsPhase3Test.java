package com.mathedu.domain.calculators.stats;

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

class StatisticsPhase3Test {

    @Test
    void descriptiveStatisticsReportAllRequestedMeasures() throws CalcException {
        var result = new StatisticsCalculator.Descriptive().calculate(
                new StatisticsCalculator.Descriptive.Input("2, 4, 4, 6", 75));

        assertThat(result.value()).isEqualTo(4);
        assertThat(result.steps()).anyMatch(step -> step.contains("Mediana: 4.0"));
        assertThat(result.steps()).anyMatch(step -> step.contains("Moda(s): [4.0]"));
        assertThat(result.steps()).anyMatch(step -> step.contains("Varianza muestral: 2.666"));
        assertThat(result.steps()).anyMatch(step -> step.contains("Rango: 2.0 a 6.0"));
        assertThat(result.steps()).anyMatch(step -> step.contains("Percentil 75.0"));
    }

    @Test
    void combinatoricsProbabilitiesAndDistributionsMatchReferenceValues() throws CalcException {
        assertThat(new StatisticsCalculator.Combination().calculate(
                new StatisticsCalculator.Combination.Input(5, 2)).value()).isEqualTo(10);
        assertThat(new StatisticsCalculator.Permutation().calculate(
                new StatisticsCalculator.Permutation.Input(5, 2)).value()).isEqualTo(20);
        assertThat(new StatisticsCalculator.BasicProbability().calculate(
                new StatisticsCalculator.BasicProbability.Input(2, 10)).value()).isEqualTo(0.2);
        assertThat(new StatisticsCalculator.ConditionalProbability().calculate(
                new StatisticsCalculator.ConditionalProbability.Input(0.4, 0.5)).value())
                .isCloseTo(0.8, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new StatisticsCalculator.Binomial().calculate(
                new StatisticsCalculator.Binomial.Input(10, 3, 0.5)).value())
                .isCloseTo(0.1171875, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new StatisticsCalculator.Normal().calculate(
                new StatisticsCalculator.Normal.Input(0, 1, 0)).value())
                .isCloseTo(0.5, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new StatisticsCalculator.Poisson().calculate(
                new StatisticsCalculator.Poisson.Input(3, 2)).value())
                .isCloseTo(0.2240418077, org.assertj.core.data.Offset.offset(1e-10));
    }

    @Test
    void validatesProbabilityAndPercentileDomains() {
        assertThatThrownBy(() -> new StatisticsCalculator.ConditionalProbability().calculate(
                new StatisticsCalculator.ConditionalProbability.Input(0.2, 0)))
                .isInstanceOf(CalcException.class);
        assertThatThrownBy(() -> new StatisticsCalculator.Descriptive().calculate(
                new StatisticsCalculator.Descriptive.Input("1, 2", 101)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void statisticsAreRegisteredAndAvailableFromTheDynamicForm() throws CalcException {
        var service = new CalculationService(AppConfig.buildRegistry());
        var input = new FormulaInputFactory().buildInput(StatisticsCalculator.Binomial.FORMULA_ID,
                Map.of("n", "10", "k", "3", "p", "0.5"),
                LengthUnit.CM, AngleUnit.DEG);

        assertThat(service.formulas().stream()
                .filter(formula -> formula.category().equals("Estadística y probabilidad")))
                .hasSize(8);
        assertThat(service.compute(StatisticsCalculator.Binomial.FORMULA_ID, input).value())
                .isCloseTo(0.1171875, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(AppCategory.STATS.isAvailable()).isTrue();
    }
}