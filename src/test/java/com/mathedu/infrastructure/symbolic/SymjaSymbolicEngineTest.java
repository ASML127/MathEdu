package com.mathedu.infrastructure.symbolic;

import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Offset.offset;

/**
 * Pruebas del adaptador real de Symja (no con dobles): normalizacion de
 * sintaxis, lista blanca de simbolos, limites de entrada y timeout.
 */
class SymjaSymbolicEngineTest {

    private final SymjaSymbolicEngine engine = new SymjaSymbolicEngine();

    @Test
    void differentiatesRealExpressions() throws CalcException {
        assertThat(engine.differentiate("x^3", "x", 1))
                .contains("3").contains("x");
        assertThat(engine.differentiate("x^2", "x", 2)).contains("2");
        assertThat(engine.differentiate("x*x", "x", 1)).contains("2");
    }

    @Test
    void integratesAndSumsDefiniteIntegrals() throws CalcException {
        assertThat(engine.integrate("x^2", "x")).contains("x");
        assertThat(engine.integrate("sin(x)", "x")).containsIgnoringCase("cos");

        assertThat(engine.integrateDefinite("x^2", "x", 0, 1))
                .isCloseTo(1.0 / 3, offset(1e-12));
        assertThat(engine.integrateDefinite("x^2", "x", 1, 0))
                .isCloseTo(-1.0 / 3, offset(1e-12));
        assertThat(engine.integrateDefinite("2*x", "x", 1, 4))
                .isCloseTo(15.0, offset(1e-12));
    }

    @Test
    void numericResultsKeepPrecisionInScientificNotation() throws CalcException {
        // Symja imprime estos resultados como 1.38*10^-11 y 7.22*10^86.
        assertThat(engine.integrateDefinite("1/x^2", "x", 1, 2))
                .isCloseTo(0.5, offset(1e-12));
        assertThat(engine.integrateDefinite("x^2", "x", 0.5, 1.5))
                .isCloseTo(13.0 / 12.0, offset(1e-15));
        assertThat(engine.limit("exp(x)", "x", 200))
                .isCloseTo(Math.exp(200), withinPercentage(1e-12));
        assertThat(engine.limit("exp(x)", "x", 0.5))
                .isCloseTo(Math.exp(0.5), offset(1e-15));
        assertThat(SymjaSymbolicEngine.toDouble("3.3333333333333333333*10^-1"))
                .isCloseTo(1.0 / 3, offset(1e-15));
        assertThat(SymjaSymbolicEngine.toDouble("7.2259737681257492581*10^86"))
                .isCloseTo(Math.exp(200), withinPercentage(1e-12));
        assertThat(SymjaSymbolicEngine.toDouble("2.5")).isEqualTo(2.5);
    }

    @Test
    void nonNumericAndNonFiniteResultsAreReportedAsBadExpressions() {
        assertThatThrownBy(() -> SymjaSymbolicEngine.toDouble("Indeterminate"))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.BAD_EXPRESSION);
        assertThatThrownBy(() -> SymjaSymbolicEngine.toDouble("ComplexInfinity"))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.BAD_EXPRESSION);
        assertThatThrownBy(() -> SymjaSymbolicEngine.toDouble("I*(-3.14)"))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void computesLimitsAndSimplifies() throws CalcException {
        assertThat(engine.limit("(x^2-1)/(x-1)", "x", 1))
                .isCloseTo(2.0, offset(1e-9));
        assertThat(engine.simplify("(x+1)^2")).contains("x");
    }

    @Test
    void acceptsSpanishOperatorsAndPi() throws CalcException {
        assertThat(engine.differentiate("3×x^2", "x", 1)).contains("6");
        assertThat(engine.differentiate("6÷x", "x", 1)).contains("6");
        assertThat(engine.limit("sin(x)/x", "x", 0)).isCloseTo(1.0, offset(1e-9));
    }

    @Test
    void rejectsSymbolsOutsideTheWhitelist() {
        assertThatThrownBy(() -> engine.differentiate("y + 1", "x", 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.BAD_EXPRESSION);
    }

    @Test
    void rejectsDisallowedCharactersAndEmptyInput() {
        assertThatThrownBy(() -> engine.differentiate("x & y", "x", 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.BAD_EXPRESSION);
        assertThatThrownBy(() -> engine.differentiate("   ", "x", 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> engine.differentiate(null, "x", 1))
                .isInstanceOf(CalcException.class);
    }

    @Test
    void enforcesLengthOrderAndVariableLimits() {
        String tooLong = "x".repeat(SymjaSymbolicEngine.MAX_EXPRESSION_LENGTH + 1);
        assertThatThrownBy(() -> engine.differentiate(tooLong, "x", 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);

        assertThatThrownBy(() -> engine.differentiate("x^2", "x", 0))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);
        assertThatThrownBy(() -> engine.differentiate("x^2", "x", 11))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_RANGE);

        assertThatThrownBy(() -> engine.differentiate("x^2", "1x", 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> engine.integrateDefinite("x^2", "x",
                Double.NaN, 1))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }
}
