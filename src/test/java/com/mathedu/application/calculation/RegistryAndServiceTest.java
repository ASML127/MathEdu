package com.mathedu.application.calculation;

import com.mathedu.domain.calculators.geometry.CircleAreaCalculator;
import com.mathedu.domain.calculators.geometry.CircleAreaInput;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegistryAndServiceTest {

    @Test
    void registryRegistraYRecupera() throws CalcException {
        CalculatorRegistry registry = new CalculatorRegistry();
        registry.register(new CircleAreaCalculator());
        assertThat(registry.contains(CircleAreaCalculator.FORMULA_ID)).isTrue();
        assertThat(registry.get(CircleAreaCalculator.FORMULA_ID)).isNotNull();
    }

    @Test
    void registryFormulaDesconocida() {
        CalculatorRegistry registry = new CalculatorRegistry();
        assertThatThrownBy(() -> registry.get("no.existe"))
                .isInstanceOf(CalcException.class)
                .extracting(e -> ((CalcException) e).getCode())
                .isEqualTo(ErrorCode.UNKNOWN_FORMULA);
    }

    @Test
    void serviceComputaYProtegeNaN() throws CalcException {
        CalculatorRegistry registry = new CalculatorRegistry();
        registry.register(new CircleAreaCalculator());
        CalculationService service = new CalculationService(registry);

        var ok = service.compute(
                CircleAreaCalculator.FORMULA_ID, new CircleAreaInput(3, LengthUnit.CM));
        assertThat(ok.value()).isCloseTo(28.2743338823,
                org.assertj.core.data.Offset.offset(1e-6));

        assertThatThrownBy(() -> service.compute(
                CircleAreaCalculator.FORMULA_ID, new CircleAreaInput(-2, LengthUnit.CM)))
                .isInstanceOf(CalcException.class);
    }
}
