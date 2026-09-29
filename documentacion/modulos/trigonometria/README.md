# Trigonometría

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

El módulo registra 13 operaciones: seno, coseno, tangente, sus tres inversas,
conversión de unidades angulares, identidades básicas, dos despejes de Pitágoras
y resolución de triángulos SSS, SAS y ASA.

La entrada angular usa `AngleUnit` (grados o radianes); el adaptador de entradas
convierte los valores cuando la calculadora espera grados. En EvalEx, `SIN`,
`COS` y `TAN` son grados; `SINR`, `COSR` y `TANR` son radianes. Para triángulos,
`TriangleSolver` concentra la solución de los casos compatibles y las
calculadoras devuelven pasos explicativos.

## Código y pruebas

- Calculadoras: `domain/calculators/trig/`.
- Resolver: `domain/trig/TriangleSolver.java`.
- Unidad: `shared/units/AngleUnit.java`.
- Registro: `AppConfig.registerTrigonometry()`.
- Pruebas: `TrigonometryPhase2Test`.

Más información: [unidades y validación](../../componentes/dominio/README.md),
[motores](../../componentes/infraestructura/README.md),
[flujo de cálculo](../../docs/flujo-de-calculo.md),
[índice principal](../../README.md).