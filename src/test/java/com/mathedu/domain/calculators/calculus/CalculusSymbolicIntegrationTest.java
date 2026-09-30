package com.mathedu.domain.calculators.calculus;

import com.mathedu.app.AppConfig;
import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

/**
 * De extremo a extremo con los motores reales (Symja + EvalEx) a traves del
 * registro y la fabrica de inputs, que es como la UI los invoca.
 *
 * <p>Las pruebas de {@link CalculusPhase5Test} usan dobles para aislar la
 * logica; aqui se comprueba que el cableado completo tambien funciona.
 */
class CalculusSymbolicIntegrationTest {

    private final CalculationService service =
            new CalculationService(AppConfig.buildRegistry());
    private final FormulaInputFactory factory = new FormulaInputFactory();

    private com.mathedu.shared.result.CalculationResult compute(String id, Map<String, String> raw)
            throws CalcException {
        Object input = factory.buildInput(id, raw, LengthUnit.CM, AngleUnit.DEG);
        return service.compute(id, input);
    }

    @Test
    void differentiatesAndEvaluatesWithSymja() throws CalcException {
        var result = compute(CalculusCalculator.Derivative.FORMULA_ID,
                Map.of("expression", "x^3 + 2*x", "variable", "x", "point", "2", "order", "1"));

        assertThat(result.value()).isCloseTo(14.0, offset(1e-12));
        assertThat(result.exact()).isTrue();
        assertThat(result.formula()).contains("3").contains("x^2");

        var second = compute(CalculusCalculator.Derivative.FORMULA_ID,
                Map.of("expression", "x^4", "variable", "x", "point", "3", "order", "2"));
        assertThat(second.value()).isCloseTo(108.0, offset(1e-12));
    }

    @Test
    void integratesDefiniteAndIndefiniteWithSymja() throws CalcException {
        var definite = compute(CalculusCalculator.DefiniteIntegral.FORMULA_ID,
                Map.of("expression", "x^2", "variable", "x", "lower", "0", "upper", "1"));
        assertThat(definite.value()).isCloseTo(1.0 / 3, offset(1e-12));
        assertThat(definite.exact()).isTrue();
        assertThat(definite.warning()).isNull();

        var antiderivative = compute(CalculusCalculator.Antiderivative.FORMULA_ID,
                Map.of("expression", "x^2", "variable", "x", "point", "3"));
        assertThat(antiderivative.value()).isCloseTo(9.0, offset(1e-12));
        assertThat(antiderivative.warning()).contains("constante de integración C");
        assertThat(antiderivative.exact()).isTrue();
    }

    @Test
    void resolvesLimitsAndRejectsBadSymbols() throws CalcException {
        var limit = compute("series.limit",
                Map.of("expression", "(x^2-1)/(x-1)", "variable", "x", "point", "1"));
        assertThat(limit.value()).isCloseTo(2.0, offset(1e-12));

        assertThatThrownBy(() -> compute(CalculusCalculator.Derivative.FORMULA_ID,
                Map.of("expression", "y^2", "variable", "x", "point", "1", "order", "1")))
                .isInstanceOf(CalcException.class)
                .hasMessageContaining("y");
    }

    @Test
    void rejectsMalformedVariableWithoutFallingIntoApproximation() {
        // Sin esta validacion el respaldo numerico recibiria una variable
        // invalida y fallaria con una excepcion no controlada.
        assertThatThrownBy(() -> new CalculusCalculator.Antiderivative(
                new FailingSymbolicEngine(), new com.mathedu.infrastructure.engine.EvalExEngine())
                .calculate(new CalculusCalculator.FunctionPointInput("x^2", "1x", 3)))
                .isInstanceOf(CalcException.class)
                .hasMessageContaining("variable");
    }

    /** Motor simbolico que siempre falla, para forzar el respaldo numerico. */
    private static final class FailingSymbolicEngine
            implements com.mathedu.domain.engine.SymbolicEngine {

        @Override public String simplify(String expression) { return expression; }
        @Override public String differentiate(String expression, String variable, int order) {
            throw new IllegalStateException("no deberia usarse");
        }
        @Override public String integrate(String expression, String variable) throws CalcException {
            throw new CalcException(com.mathedu.shared.errors.ErrorCode.BAD_EXPRESSION, "expr", "sin forma");
        }
        @Override public double integrateDefinite(String expression, String variable,
                double lower, double upper) {
            throw new IllegalStateException("no deberia usarse");
        }
        @Override public double limit(String expression, String variable, double point) {
            throw new IllegalStateException("no deberia usarse");
        }
    }
}
