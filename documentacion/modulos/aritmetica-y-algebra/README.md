# Aritmética y álgebra

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

El módulo reúne operaciones numéricas, manipulación algebraica, ecuaciones y
polinomios; registra 28 calculadoras.

## Operaciones

- Aritmética: suma, resta, multiplicación, división y evaluación de expresiones.
- Proporcionalidad: porcentaje, porcentaje de una cantidad y regla de tres.
- Números: operaciones con fracciones, potencias, raíces, logaritmos,
  exponenciales y valor absoluto.
- Ecuaciones: lineal (`ax + b = c`), cuadrática, sistemas 2×2 y 3×3 e
  inecuación lineal.
- Polinomios: evaluar, factorizar, sumar, restar, multiplicar y dividir.
- Identidades: cuadrado de binomio, diferencia de cuadrados y suma por
  diferencia.

La evaluación general usa EvalEx a través de `ExpressionEngine`. El motor admite
variables y funciones documentadas en [Motores](../../componentes/infraestructura/README.md).
La factorización actual se limita a coeficientes enteros y grado máximo 2; no
cubre coeficientes no enteros ni raíces complejas.

## Código y pruebas

- Implementaciones: `domain/calculators/algebra/`.
- Modelos de apoyo: `domain/algebra/` (`Fraction`, `Polynomial`, `LinearSystems`).
- Registro y motor: `AppConfig.registerAlgebra()`.
- Conversión de inputs: `FormulaInputFactory`.
- Pruebas: `AlgebraPhase2Test`, además de pruebas de `EvalExEngine`.

Véase [validación de dominio](../../componentes/dominio/README.md),
[guía de extensión](../../docs/como-anadir-formulas.md) y el
[índice principal](../../README.md).