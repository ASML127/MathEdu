package com.mathedu.domain.calculators.trig;

import com.mathedu.domain.trig.TriangleSolver;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrigonometryPhase2Test {

    @Test
    void evaluatesBasicAndInverseTrigonometricFunctions() throws CalcException {
        assertThat(new SinCalculator().calculate(new SinCalculator.Input(30)).value())
                .isCloseTo(0.5, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new CosCalculator().calculate(new CosCalculator.Input(60)).value())
                .isCloseTo(0.5, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new TanCalculator().calculate(new TanCalculator.Input(45)).value())
                .isCloseTo(1, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new AsinCalculator().calculate(
                new AsinCalculator.Input(0.5, AngleUnit.DEG)).value())
                .isCloseTo(30, org.assertj.core.data.Offset.offset(1e-12));
        assertThatThrownBy(() -> new TanCalculator().calculate(new TanCalculator.Input(90)))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void convertsAngleUnitsAndChecksIdentities() throws CalcException {
        assertThat(new AngleConvertCalculator().calculate(
                new AngleConvertCalculator.Input(Math.PI, AngleUnit.RAD)).value())
                .isCloseTo(180, org.assertj.core.data.Offset.offset(1e-12));
        assertThat(new IdentitiesCalculator().calculate(
                new IdentitiesCalculator.Input(30)).value())
                .isCloseTo(1, org.assertj.core.data.Offset.offset(1e-12));
    }

    @Test
    void solvesRightTriangleAndSssWithExplanatorySteps() throws CalcException {
        assertThat(new PythagorasHypCalculator().calculate(
                new PythagorasHypCalculator.Input(3, 4, LengthUnit.CM)).value()).isEqualTo(5);

        var result = new SolveSssCalculator().calculate(
                new SolveSssCalculator.Input(3, 4, 5, LengthUnit.CM, AngleUnit.DEG));
        assertThat(result.value()).isCloseTo(36.8698976458,
                org.assertj.core.data.Offset.offset(1e-8));
        assertThat(result.steps()).hasSizeGreaterThanOrEqualTo(5);
        assertThat(result.steps()).anyMatch(step -> step.contains("Teorema del coseno"));
    }

        @Test
        void solvesSasAndAsaTriangles() throws CalcException {
                var sas = new SolveSasCalculator().calculate(
                                new SolveSasCalculator.Input(3, 4, 90, LengthUnit.CM, AngleUnit.DEG));
                assertThat(sas.value()).isCloseTo(5, org.assertj.core.data.Offset.offset(1e-12));
                assertThat(sas.steps()).anyMatch(step -> step.contains("Teorema del seno"));

                var asa = new SolveAsaCalculator().calculate(
                                new SolveAsaCalculator.Input(30, 60, 5, LengthUnit.CM, AngleUnit.DEG));
                assertThat(asa.value()).isCloseTo(2.5, org.assertj.core.data.Offset.offset(1e-12));
                assertThat(asa.steps()).anyMatch(step -> step.contains("b = 4.330"));
        }

    @Test
    void rejectsImpossibleTriangle() {
        assertThatThrownBy(() -> TriangleSolver.sss(1, 2, 10))
                .isInstanceOf(CalcException.class)
                .extracting(error -> ((CalcException) error).getCode())
                .isEqualTo(ErrorCode.DOMAIN_ERROR);
    }
}