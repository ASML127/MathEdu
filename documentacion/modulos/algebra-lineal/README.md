# Álgebra lineal

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

Diez operaciones sobre vectores y matrices, implementadas con Apache Commons
Math:

- Suma, resta y producto escalar de vectores.
- Suma, resta y producto de matrices.
- Determinante, matriz inversa, rango y solución de sistema cuadrado.

Los vectores se escriben con componentes separadas por coma, punto y coma o
espacios. Las matrices se escriben por filas con `;` entre filas y coma o espacio
entre componentes, por ejemplo `1,2;3,4`. La dimensión máxima es 50. Las
operaciones binarias requieren dimensiones compatibles; determinante, inversa y
Gauss requieren matriz cuadrada; la inversa y Gauss rechazan matrices singulares.

El solver usa descomposición LU con pivoteo para el sistema. La operación Gauss
presenta el procedimiento como eliminación con pivoteo parcial.

## Código y pruebas

- Implementación: `domain/calculators/linear/LinearAlgebraCalculator.java`.
- Dependencia: Apache Commons Math 3.
- Registro: `AppConfig.registerLinearAlgebra()`.
- Pruebas: `LinearAlgebraPhase4Test`.

Relacionado: [motores y dependencias](../../componentes/infraestructura/README.md),
[pruebas](../../docs/pruebas.md), [índice principal](../../README.md).