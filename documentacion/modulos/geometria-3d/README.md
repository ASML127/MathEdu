# Geometría 3D

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

Una implementación parametrizada por `SolidMetricsCalculator.Kind` registra 10
sólidos: cubo, prisma rectangular, prisma regular, pirámide regular, cilindro,
cono, tronco de cono, esfera, hemisferio y tetraedro regular.

Se calculan área lateral, área total y volumen; el volumen es el resultado
principal y la unidad se expresa elevada al cubo. Las dimensiones deben ser
positivas; el radio mayor del tronco debe exceder el menor y los polígonos
regulares admiten de 3 a 10 000 lados.

## Código y pruebas

- Implementación y lista de sólidos: `domain/calculators/solid/SolidMetricsCalculator.java`.
- Registro por enum: `AppConfig.registerSolids()`.
- Construcción de inputs: despacho por ID al inicio de `FormulaInputFactory`.
- Pruebas: `SolidMetricsPhase3Test`.

Relacionado: [unidades](../../componentes/dominio/README.md),
[catálogo completo](../README.md), [índice principal](../../README.md).