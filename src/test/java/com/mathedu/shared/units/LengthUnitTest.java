package com.mathedu.shared.units;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LengthUnitTest {

    @Test
    void conversionIdaVuelta() {
        assertThat(LengthUnit.convert(100, LengthUnit.CM, LengthUnit.M)).isEqualTo(1.0);
        assertThat(LengthUnit.convert(1, LengthUnit.KM, LengthUnit.M)).isEqualTo(1000.0);
        assertThat(LengthUnit.CM.areaSymbol()).isEqualTo("cm^2");
        assertThat(LengthUnit.M.volumeSymbol()).isEqualTo("m^3");
    }
}
