# Geometría analítica

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

Las 10 operaciones cubren distancia entre puntos, punto medio, pendiente, recta
por dos puntos, forma punto-pendiente, forma pendiente-ordenada al origen,
conversión desde la forma general, intersección de rectas, distancia de un punto
a una recta y ecuación de circunferencia.

El módulo se implementa como clases anidadas de
`AnalyticGeometryCalculator`; cada tipo define sus inputs y metadatos
`FormulaDef`. Las validaciones evitan degeneraciones como rectas paralelas en
operaciones que requieren intersección y resultados no finitos.

## Código y pruebas

- Implementación: `domain/calculators/analytic/AnalyticGeometryCalculator.java`.
- Registro: `AppConfig.registerAnalyticGeometry()`.
- Adaptación de entradas: `FormulaInputFactory`.
- Pruebas: `AnalyticGeometryPhase3Test`.

Consulta [dominio y errores](../../componentes/dominio/README.md),
[pruebas](../../docs/pruebas.md) o vuelve al [índice](../../README.md).