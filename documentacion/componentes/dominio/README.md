# Dominio, modelos y contratos

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

## Estructura

- `domain/calculators/`: contrato `Calculator<T>` y familias de operaciones.
- `domain/formulas/`: `FormulaDef` y `InputDef`, metadatos que permiten generar
  el formulario sin que la vista conozca cada record de entrada.
- `domain/shapes/`: abstracciones y modelos de figuras planas.
- `domain/algebra/`: fracciones, polinomios y sistemas lineales reutilizables.
- `domain/trig/`: solucionador de triángulos.
- `domain/validation/`: `InputParser` para convertir texto y `Guards` para
  restricciones compartidas, magnitudes y resultados finitos.
- `domain/engine/`: contratos `ExpressionEngine` y `SymbolicEngine`; el dominio
  no depende de EvalEx ni Symja directamente.
- `domain/history/`: entidad `HistoryEntry` persistida por la aplicación.

## Resultado y errores

`CalculationResult` contiene valor, etiqueta, expresión/fórmula, pasos, unidad,
indicador de exactitud y una nota opcional. `CalcException` acompaña un
`ErrorCode` y puede identificar un campo para mostrar el error en línea. El
resultado principal debe ser finito; restricciones reutilizables se centralizan
en `Guards`, y validaciones específicas permanecen junto al calculador.

## Unidades

`LengthUnit` convierte/representa mm, cm, m, km, in y ft, con símbolos para
longitud, área y volumen. `AngleUnit` representa grados y radianes. El límite
entre formularios y entradas está en `FormulaInputFactory`.

Las páginas funcionales están en [módulos](../../modulos/README.md). Las pruebas
por dominio aparecen en [Pruebas](../../docs/pruebas.md). Volver al
[índice principal](../../README.md).