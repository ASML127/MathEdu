# Servicios de aplicación

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

## Cálculo

- `CalculatorRegistry` mantiene un mapa ordenado por `formulaId`, registra
  `Calculator<?>` y rechaza IDs desconocidos con `CalcException`.
- `CalculationService` obtiene el calculador, delega la operación, comprueba que
  el valor principal sea finito y expone los metadatos para la UI.
- `FormulaInputFactory` es el adaptador entre campos de texto y entradas tipadas.
  Centraliza el parseo y la conversión de unidades; las restricciones matemáticas
  pertenecen a cada calculador/guardia de dominio.

## Historial

- `HistoryRepository` es el puerto de persistencia.
- `HistoryService` mantiene hasta 500 entradas, más recientes primero, y un
  conjunto de favoritos por ID de fórmula. Sincroniza sus operaciones y delega
  cada persistencia al repositorio.
- La implementación JSON concreta está en
  [infraestructura](../infraestructura/README.md).

Pruebas: `RegistryAndServiceTest`, `FormulaInputFactoryTest` y
`HistoryServiceTest`. Recorrido completo: [Flujo de cálculo](../../docs/flujo-de-calculo.md).
Volver al [índice principal](../../README.md).