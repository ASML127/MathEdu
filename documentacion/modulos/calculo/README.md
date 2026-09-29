# Cálculo diferencial e integral

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

El módulo registra tres operaciones:

| Operación | Comportamiento |
|---|---|
| Derivada simbólica | Deriva de orden 1 a 10 y evalúa en un punto. |
| Integral indefinida | Busca una antiderivada y la evalúa; si falla Symja, integra numéricamente desde 0, fijando `F(0)=0`. |
| Integral definida | Intenta integración simbólica y recurre a cuadratura numérica si no hay resultado simbólico. |

Symja resuelve el cálculo simbólico; EvalEx evalúa las expresiones en puntos. La
integral definida prueba Gauss-Legendre adaptativo y dispone de respaldo Simpson.
Los resultados numéricos de respaldo se marcan como aproximados en
`CalculationResult`. La constante `C` de una antiderivada no cambia su derivada;
al evaluarla, el fallback fija esa constante en cero.

## Código y pruebas

- Calculadoras: `domain/calculators/calculus/CalculusCalculator.java`.
- Interfaces de motores: `domain/engine/`.
- Implementaciones: `infrastructure/symbolic/` e `infrastructure/engine/`.
- Registro: `AppConfig.registerCalculus()`.
- Pruebas: `CalculusPhase5Test`, `CalculusSymbolicIntegrationTest` y pruebas
  específicas de Symja.

Ver [motores matemáticos](../../componentes/infraestructura/README.md),
[pruebas](../../docs/pruebas.md) o el [índice principal](../../README.md).