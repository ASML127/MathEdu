package com.mathedu.infrastructure.engine;

import com.mathedu.shared.errors.CalcException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EvalExEngineTest {

    private final EvalExEngine engine = new EvalExEngine();

    @Test
    void evaluatesVariablesAndArithmetic() throws CalcException {
        assertThat(engine.evaluate("2*x + 3", Map.of("x", 2.0))).isEqualTo(7.0);
    }

    @Test
    void evaluatesTrigFunctionsInDeclaredAngleUnits() throws CalcException {
        assertThat(engine.evaluate("SIN(30)", Map.of())).isCloseTo(0.5,
                org.assertj.core.data.Offset.offset(1e-12));
        assertThat(engine.evaluate("SINR(0.5235987755982988)", Map.of())).isCloseTo(0.5,
                org.assertj.core.data.Offset.offset(1e-12));
    }

    @Test
    void rejectsMalformedAndNonFiniteExpressions() {
        assertThatThrownBy(() -> engine.evaluate("2**x", Map.of("x", 2.0)))
                .isInstanceOf(CalcException.class);
        assertThatThrownBy(() -> engine.evaluate("1/0", Map.of()))
                .isInstanceOf(CalcException.class);
    }
}