package com.mathedu.domain.calculators;

import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.result.CalculationResult;

/**
 * Contrato de toda operacion matematica (polimorfismo total).
 *
 * @param <T> tipo del input validado (record)
 */
public interface Calculator<T> {

    /** Ejecuta el calculo. Lanza {@link CalcException} con mensaje didactico si falla. */
    CalculationResult calculate(T input) throws CalcException;

    /** Metadatos para la UI dinamica. */
    FormulaDef formula();
}
