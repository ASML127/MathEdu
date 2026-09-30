# Pruebas y cobertura

[Índice de documentación](../README.md) · [Guías generales](README.md)

El proyecto usa JUnit 5 y AssertJ. Las pruebas unitarias están bajo
`src/test/java/com/mathedu/` y siguen los paquetes principales del código de
producción. JaCoCo se conecta al ciclo Maven y genera un informe durante `verify`.

## Ejecutar

```bash
mvn test
mvn clean verify
mvn -Dtest=AlgebraPhase2Test test
```

El informe HTML se genera en `target/site/jacoco/index.html`.
`mvn verify` aplica un gate mínimo del 80% de líneas cubiertas en `domain`.

Para ejecutar el smoke JavaFX en un entorno con pantalla:

```bash
mvn -Dphase6.ui=true -Dtest=MainWindowSmokeTest test
```

El workflow CI Linux lo ejecuta con Xvfb; los tests de dominio permanecen
headless.

## Mapa de pruebas

| Área | Clases de prueba |
|---|---|
| Registro y construcción de entradas | `RegistryAndServiceTest`, `FormulaInputFactoryTest` |
| Historial | `HistoryServiceTest` |
| Álgebra | `AlgebraPhase2Test` |
| Trigonometría | `TrigonometryPhase2Test` |
| Geometría analítica y sólidos | `AnalyticGeometryPhase3Test`, `SolidMetricsPhase3Test` |
| Estadística | `StatisticsPhase3Test` |
| Álgebra lineal | `LinearAlgebraPhase4Test` |
| Cálculo | `CalculusPhase5Test`, `CalculusSymbolicIntegrationTest` |
| Series | `SeriesPhase5Test` |
| Geometría plana | `CircleAreaCalculatorTest`, `PlaneAreaTest`, `PlanePerimeterTest`, `PlaneValidationTest` |
| Motor numérico y simbólico | `EvalExEngineTest`, `LazySymbolicEngineTest`, `SymjaSymbolicEngineTest` |
| Presentación | `MainViewModelTest`, `MainWindowSmokeTest` (JavaFX/TestFX, opt-in) |
| Unidades | `LengthUnitTest` |

## Criterios de cobertura

Al ampliar un calculador, prueba el caso válido, límites de dominio relevantes,
entradas incompatibles y la representación del resultado/unidad. Para cálculo,
los casos de respaldo deben verificar que un resultado aproximado se marque
como tal. Para interfaces de infraestructura, mantén pruebas de contrato sobre
parseo, errores y resultados.

Las pruebas de UI JavaFX pueden depender de un entorno gráfico; la mayoría de la
lógica del ViewModel y de dominio debe poder probarse sin iniciar una ventana.
Véase [flujo de cálculo](flujo-de-calculo.md) y [cómo extender](como-anadir-formulas.md).