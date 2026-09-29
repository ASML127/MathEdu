# Estadística y probabilidad

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

Ocho operaciones implementadas con Apache Commons Math:

- Resumen descriptivo: media, mediana, moda, varianza y desviación estándar
  muestrales, mínimo/máximo, rango y percentil.
- Combinaciones y variaciones sin repetición.
- Probabilidad clásica y condicional.
- Probabilidad puntual binomial y de Poisson.
- Distribución normal acumulada `P(X ≤ x)`.

El percentil descriptivo acepta 0–100; los datos se separan con coma, punto y
coma o espacios. Las probabilidades se comprueban contra sus dominios y los
parámetros de distribución deben ser válidos. El resultado de la distribución
normal es acumulado, no una densidad ni una probabilidad puntual.

## Código y pruebas

- Implementación: `domain/calculators/stats/StatisticsCalculator.java`.
- Dependencia numérica: `commons-math3` declarada en `pom.xml`.
- Registro: `AppConfig.registerStatistics()`.
- Pruebas: `StatisticsPhase3Test`.

Consulta [pruebas](../../docs/pruebas.md),
[validaciones comunes](../../componentes/dominio/README.md) y el
[índice principal](../../README.md).