package com.mathedu.infrastructure.symbolic;

import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.shared.errors.CalcException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

/**
 * El motor diferido debe comportarse como Symja, construirlo una sola vez y
 * no cargar Symja al construir el objeto (de ahi el nombre).
 */
class LazySymbolicEngineTest {

    @Test
    void delegatesToSymja() throws CalcException {
        SymbolicEngine engine = new LazySymbolicEngine();

        assertThat(engine.differentiate("x^2", "x", 1)).contains("2");
        assertThat(engine.integrateDefinite("x^2", "x", 0, 1))
                .isCloseTo(1.0 / 3, offset(1e-12));
        assertThat(engine.limit("(x^2-1)/(x-1)", "x", 1)).isCloseTo(2.0, offset(1e-12));
    }

    @Test
    void reusesTheSameDelegateAndKeepsErrors() throws CalcException {
        LazySymbolicEngine engine = new LazySymbolicEngine();
        engine.differentiate("x^2", "x", 1);
        assertThat(engine.differentiate("x^3", "x", 1)).contains("3");

        assertThatThrownBy(() -> engine.differentiate("z + 1", "x", 1))
                .isInstanceOf(CalcException.class)
                .hasMessageContaining("z");
    }

    @Test
    void firstOperationIsNotKilledByTheTimeout() throws CalcException {
        // El primer ExprEvaluator de Symja carga cientos de clases (~2 s). Si ese
        // coste se contara contra el timeout, la primera operacion del usuario
        // fallaria siempre con ErrorCode.TIMEOUT: esto fallaba antes de
        // sacar el warm-up de la cuenta del plazo.
        assertThat(new SymjaSymbolicEngine().differentiate("x^2", "x", 1))
                .contains("2");
    }
}
