package com.mathedu.domain.calculators.linear;

import com.mathedu.app.AppConfig;
import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LinearAlgebraPhase4Test {

    @Test
    void computesThreeByThreeDeterminantAndInverse() throws CalcException {
        String matrix = "1,2,3;0,1,4;5,6,0";
        var determinant = new LinearAlgebraCalculator.Determinant().calculate(
                new LinearAlgebraCalculator.SingleMatrixInput(matrix));
        assertThat(determinant.value()).isCloseTo(1, org.assertj.core.data.Offset.offset(1e-12));

        var inverse = new LinearAlgebraCalculator.Inverse().calculate(
                new LinearAlgebraCalculator.SingleMatrixInput(matrix));
        assertThat(inverse.value()).isCloseTo(-24,
                org.assertj.core.data.Offset.offset(1e-12));
        assertThat(inverse.steps()).anyMatch(step -> step.contains("-24.0"));
    }

    @Test
    void solvesThreeByThreeSystemWithGaussianElimination() throws CalcException {
        var result = new LinearAlgebraCalculator.SolveGauss().calculate(
                new LinearAlgebraCalculator.SystemInput("1,0,0;0,1,0;0,0,1", "1,2,3"));

        assertThat(result.value()).isEqualTo(1);
        assertThat(result.steps()).anyMatch(step -> step.contains("[1.0, 2.0, 3.0]"));
    }

    @Test
    void computesVectorMatrixOperationsAndRank() throws CalcException {
        var vector = new LinearAlgebraCalculator.VectorAdd().calculate(
                new LinearAlgebraCalculator.VectorInput("1,2,3", "4,5,6"));
        assertThat(vector.steps()).anyMatch(step -> step.contains("[5.0, 7.0, 9.0]"));
        assertThat(new LinearAlgebraCalculator.VectorDotProduct().calculate(
                new LinearAlgebraCalculator.VectorInput("1,2,3", "4,5,6")).value())
                .isEqualTo(32);

        var product = new LinearAlgebraCalculator.MatrixProduct().calculate(
                new LinearAlgebraCalculator.MatrixInput("1,2;3,4", "5,6;7,8"));
        assertThat(product.steps()).anyMatch(step -> step.contains("[[19.0, 22.0], [43.0, 50.0]]"));

        assertThat(new LinearAlgebraCalculator.Rank().calculate(
                new LinearAlgebraCalculator.SingleMatrixInput("1,2,3;2,4,6;1,1,1")).value())
                .isEqualTo(2);
    }

    @Test
    void reportsSingularMatricesAndInvalidDimensionsAsDomainErrors() {
        var singular = new LinearAlgebraCalculator.SingleMatrixInput("1,2;2,4");
        assertThatThrownBy(() -> new LinearAlgebraCalculator.Inverse().calculate(singular))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.SINGULAR_MATRIX);
        assertThatThrownBy(() -> new LinearAlgebraCalculator.SolveGauss().calculate(
                new LinearAlgebraCalculator.SystemInput("1,2;2,4", "3,6")))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.SINGULAR_MATRIX);
        assertThatThrownBy(() -> new LinearAlgebraCalculator.MatrixAdd().calculate(
                new LinearAlgebraCalculator.MatrixInput("1,2;3,4", "1,2,3")))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void operationsAreRegisteredAndReachableFromTheDynamicForm() throws CalcException {
        var service = new CalculationService(AppConfig.buildRegistry());
        var formulas = service.formulas().stream()
                .filter(formula -> formula.category().equals("Álgebra lineal")).toList();
        assertThat(formulas).hasSize(10);
        assertThat(AppCategory.LINEAR.isAvailable()).isTrue();

        var input = new FormulaInputFactory().buildInput(LinearAlgebraCalculator.Determinant.FORMULA_ID,
                Map.of("matrix", "1,2;3,4"), LengthUnit.CM, AngleUnit.DEG);
        assertThat(service.compute(LinearAlgebraCalculator.Determinant.FORMULA_ID, input).value())
                .isEqualTo(-2);
    }
}