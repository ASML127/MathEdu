package com.mathedu.domain.calculators.algebra;

import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlgebraPhase2Test {

    @Test
    void solvesLinearEquationWithBothSidesAndKeepsLegacyInput() throws CalcException {
        var calculator = new LinearEquationCalculator();
        assertThat(calculator.calculate(new LinearEquationCalculator.Input(2, 3, 7)).value())
                .isEqualTo(2);
        assertThat(calculator.calculate(new LinearEquationCalculator.Input(2, -6)).value())
                .isEqualTo(3);
        assertThatThrownBy(() -> calculator.calculate(new LinearEquationCalculator.Input(0, 3, 3)))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.INFINITE_SOLUTIONS);
    }

    @Test
    void quadraticExampleReportsBothRoots() throws CalcException {
        var result = new QuadraticEquationCalculator().calculate(
                new QuadraticEquationCalculator.Input(1, -5, 6));

        assertThat(result.value()).isEqualTo(3);
        assertThat(result.steps()).anyMatch(step -> step.contains("x2 =") && step.endsWith("= 2.0"));
    }

    @Test
    void factorsQuadraticAndRejectsInputsOutsideTheSupportedDomain() throws CalcException {
        var calculator = new PolyFactorCalculator();
        var result = calculator.calculate(new PolyFactorCalculator.Input("1, -5, 6"));

        assertThat(result.formula()).contains("(x - (3))", "(x - (2))");
        assertThat(result.steps()).anyMatch(step -> step.contains("x₁ = 3, x₂ = 2"));
        assertThatThrownBy(() -> calculator.calculate(new PolyFactorCalculator.Input("1, 0, 0, -1")))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
        assertThatThrownBy(() -> calculator.calculate(new PolyFactorCalculator.Input("1, 0, 1")))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);
    }

    @Test
    void factorizationIsAvailableFromDynamicForm() throws CalcException {
        Object input = new FormulaInputFactory().buildInput(PolyFactorCalculator.FORMULA_ID,
                Map.of("coeffs", "1, -5, 6"), LengthUnit.CM, AngleUnit.DEG);

        assertThat(input).isInstanceOf(PolyFactorCalculator.Input.class);
        assertThat(new PolyFactorCalculator().calculate((PolyFactorCalculator.Input) input)
                .formula()).contains("(x - (3))", "(x - (2))");
    }

    @Test
    void polynomialAdditionReturnsTheResultingExpression() throws CalcException {
        var result = new PolyAddCalculator().calculate(
                new PolyAddCalculator.Input("1, -2, 1", "2, 3"));

        assertThat(result.steps()).anyMatch(step -> step.contains("R(x) = x^2 + 4"));
    }

    @Test
    void arithmeticFractionsPowersRootsLogsAndInequalitiesWork() throws CalcException {
        assertThat(new AddCalculator().calculate(new AddCalculator.Input(2, 3)).value()).isEqualTo(5);
        assertThat(new DivideCalculator().calculate(new DivideCalculator.Input(8, 4)).value())
                .isEqualTo(2);
        assertThat(new FractionsCalculator().calculate(
                new FractionsCalculator.Input(1, 2, "+", 1, 4)).value()).isEqualTo(0.75);
        assertThat(new PowerCalculator().calculate(new PowerCalculator.Input(2, 5)).value())
                .isEqualTo(32);
        assertThat(new RootCalculator().calculate(new RootCalculator.Input(27, 3)).value())
                .isCloseTo(3, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new LogCalculator().calculate(new LogCalculator.Input(100, 10)).value())
                .isCloseTo(2, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new InequalityCalculator().calculate(
                new InequalityCalculator.Input(2, -4, ">")).steps())
                .anyMatch(step -> step.contains("x > 2.0"));
    }

    @Test
    void systemsAndPolynomialDivisionProduceExpectedSolutions() throws CalcException {
        var system2 = new System2x2Calculator().calculate(
                new System2x2Calculator.Input(2, 3, 7, 1, -1, 1));
        assertThat(system2.value()).isCloseTo(2, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(system2.steps()).anyMatch(step -> step.contains("y = 1.0"));

        var system3 = new System3x3Calculator().calculate(
                new System3x3Calculator.Input(1, 0, 0, 1, 0, 1, 0, 2, 0, 0, 1, 3));
        assertThat(system3.value()).isCloseTo(1, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(system3.steps()).anyMatch(step -> step.contains("y = 2.0, z = 3.0"));

        var division = new PolyDivideCalculator().calculate(
                new PolyDivideCalculator.Input("1, 0, -1", "1, -1"));
        assertThat(division.steps()).anyMatch(step -> step.contains("C(x) = x + 1"));
        assertThat(division.steps()).anyMatch(step -> step.contains("R(x) = 0"));
    }

    @Test
    void percentagesRuleOfThreeAndPolynomialOperationsWork() throws CalcException {
        assertThat(new SubtractCalculator().calculate(new SubtractCalculator.Input(8, 3)).value())
                .isEqualTo(5);
        assertThat(new MultiplyCalculator().calculate(new MultiplyCalculator.Input(2, 3)).value())
                .isEqualTo(6);
        assertThat(new PercentCalculator().calculate(new PercentCalculator.Input(15, 200)).value())
                .isEqualTo(30);
        assertThat(new PercentOfCalculator().calculate(new PercentOfCalculator.Input(30, 200)).value())
                .isEqualTo(15);
        assertThat(new RuleOfThreeCalculator().calculate(
                new RuleOfThreeCalculator.Input(4, 10, 6)).value()).isEqualTo(15);

        var difference = new PolySubtractCalculator().calculate(
                new PolySubtractCalculator.Input("1, 0, -1", "1, -1"));
        assertThat(difference.steps()).anyMatch(step -> step.contains("R(x) = x^2 - x"));
        var product = new PolyMultiplyCalculator().calculate(
                new PolyMultiplyCalculator.Input("1, 1", "1, -1"));
        assertThat(product.steps()).anyMatch(step -> step.contains("R(x) = x^2 - 1"));
        var notable = new BinomialSquareCalculator().calculate(
                new BinomialSquareCalculator.Input(3, 2));
        assertThat(notable.value()).isEqualTo(25);
    }
}