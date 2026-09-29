# Infraestructura y motores

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

La infraestructura implementa contratos definidos en el dominio y se ensambla
desde `AppConfig`.

## Evaluación numérica

`EvalExEngine` implementa `ExpressionEngine`. Normaliza `×`, `÷` y coma decimal,
admite constantes `PI`/`E`, variables, `SQRT`, `LOG`, `LOG10`, `ABS`, potencias,
trigonometría y radianes mediante `SINR`, `COSR`, `TANR`. La coma decimal se
normaliza solo entre dígitos; no se admiten comas como separador de argumentos.
Los errores de parseo/evaluación se traducen a `CalcException`.

## Evaluación simbólica

`SymjaSymbolicEngine` adapta Symja para simplificación, derivación, integración y
límite. `LazySymbolicEngine` inicializa Symja en el primer uso y publica una
instancia compartida de forma segura. Los calculadores de cálculo y series pueden
recurrir a métodos numéricos cuando procede; esos resultados se identifican como
aproximados. Los detalles por funcionalidad están en [Cálculo](../../modulos/calculo/README.md)
y [Series](../../modulos/series-y-sucesiones/README.md).

## Persistencia

`JsonHistoryRepository` implementa `HistoryRepository` con Jackson. Usa
`~/.mathlab-edu/history.json`, devuelve un historial vacío si falta o no puede
leerse el archivo, y escribe en temporal antes de reemplazar el destino.

## Logging y dependencias

SLF4J es la API y Logback el backend, configurado en
`src/main/resources/logback.xml`. `pom.xml` fija SLF4J 2.x porque Symja arrastra
una versión 1.7.x que no es compatible con el descubrimiento esperado por
Logback 1.5.x.

Pruebas: `EvalExEngineTest`, `LazySymbolicEngineTest`, `SymjaSymbolicEngineTest`
y pruebas de historial. Véase [instalación](../../docs/instalacion-y-ejecucion.md)
o el [índice principal](../../README.md).