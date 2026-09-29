# Series y sucesiones

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

El área registra ocho operaciones:

- Sucesión aritmética y geométrica: término n y suma parcial.
- Serie geométrica infinita, convergente solo cuando `|r| < 1`.
- Suma parcial de una expresión evaluada en un intervalo entero.
- Criterio de la razón y criterio de la raíz: convergencia absoluta si `L < 1`,
  divergencia si `L > 1`, inconcluso si `L = 1`.
- Comparación por límite bajo los supuestos de términos no negativos y modo de
  comparación indicado.
- Límite de función: intento simbólico y, si no se resuelve, aproximación
  bilateral numérica marcada como aproximada.

Las sucesiones y sumas limitan el número de términos a 100 000. La aproximación
bilateral de un límite es heurística y no demuestra por sí sola la existencia del
límite.

## Código y pruebas

- Implementación: `domain/calculators/series/SeriesCalculator.java`.
- Motores usados: `ExpressionEngine` y `SymbolicEngine`.
- Registro: `AppConfig.registerSeries()`.
- Pruebas: `SeriesPhase5Test` y pruebas de motores simbólicos.

Véase [motores](../../componentes/infraestructura/README.md),
[pruebas](../../docs/pruebas.md) y el [índice principal](../../README.md).