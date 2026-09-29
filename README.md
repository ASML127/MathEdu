# MathLab Edu — Fase 3 implementada

Calculadora y asistente de estudio matemático (Java 21 + JavaFX + Maven).

## Estado

- [x] Fase 0: bootstrap, arquitectura, círculo demo
- [x] Fase 1: geometría plana, unidades de longitud, formulario dinámico e historial JSON
- [x] Fase 2: aritmética, álgebra, polinomios, ecuaciones y trigonometría
- [x] Fase 3: geometría analítica, sólidos 3D y estadística
- [x] 100 fórmulas registradas: 31 plana, 28 álgebra, 13 trigonometría, 10 analítica, 10 sólidos y 8 estadísticas
- [x] Formulario dinámico genérico desde `FormulaDef` (añadir fórmula = 1 clase + `register()`)
- [x] Validación centralizada: `Guards` (dominio) + `InputParser` (texto UI, coma decimal)
- [x] Unidades: `LengthUnit` (mm/cm/m/km/in/ft) + `AngleUnit` (grados/radianes)
- [x] Historial JSON `~/.mathlab-edu/history.json`: re-ejecutar, favoritos, quitar, limpiar
- [x] 85 pruebas automatizadas aprobadas (`mvn verify`)

### Aritmética y álgebra

Operaciones básicas, porcentajes, regla de tres, fracciones, potencias, raíces,
logaritmos, exponenciales, valor absoluto, ecuaciones lineales y cuadráticas,
sistemas 2×2 y 3×3, desigualdades lineales, productos notables y evaluación y
operaciones con polinomios. La ecuación lineal admite `ax + b = c`.

El motor EvalEx evalúa expresiones con variables y funciones; `SIN` usa grados y
las funciones `SINR`, `COSR` y `TANR` usan radianes. La factorización añadida
acepta coeficientes enteros y polinomios reales de grado máximo 2; otros grados,
coeficientes no enteros y raíces complejas quedan fuera del alcance actual.

### Trigonometría

Seno, coseno y tangente con inversas, conversión entre grados y radianes,
identidades básicas, teorema de Pitágoras y resolución de triángulos SSS, SAS y
ASA, con pasos explicativos.

### Geometría analítica

Distancia, punto medio, pendiente, recta por dos puntos, formas punto-pendiente,
pendiente-ordenada y general con conversiones, intersección de rectas, distancia
de punto a recta y ecuación de circunferencia.

### Geometría 3D

Cubo, prisma rectangular, prisma regular, pirámide regular, cilindro, cono,
tronco de cono, esfera, hemisferio y tetraedro regular. Cada cálculo muestra
área lateral, área total y volumen; el volumen es el resultado principal y usa
la unidad de longitud seleccionada elevada al cubo.

### Estadística y probabilidad

Resumen descriptivo (media, mediana, moda, varianza y desviación estándar
muestrales, rango y percentil), combinaciones, variaciones sin repetición,
probabilidad clásica y condicional, y probabilidades puntuales binomial y
Poisson. La normal devuelve la probabilidad acumulada `P(X ≤ x)`. Los cálculos
usan Apache Commons Math.

Figuras: cuadrado, rectángulo, triángulo (base·altura / Herón / perímetro),
equilátero, isósceles, triángulo rectángulo, paralelogramo, rombo, trapecio, cometa,
círculo, semicírculo, sector (área + arco), corona, polígono regular.

## Requisitos

- JDK 21 (Temurin/Adoptium u OpenJDK). El proyecto fija `maven.compiler.release=21`.
- Maven 3.9+.

> Nota de este entorno: solo había JRE 25 sin `javac` ni Maven del sistema.
> Se usaron: JDK 21 en `/tmp/opencode/jdk-21.0.12.1+1` y Maven en
> `/tmp/opencode/apache-maven-3.9.9`. Si instalas JDK 21 + Maven del sistema,
> los comandos de abajo funcionan sin `export`.

## Comandos

```bash
export JAVA_HOME=/tmp/opencode/jdk-21.0.12.1+1
export PATH=$JAVA_HOME/bin:/tmp/opencode/apache-maven-3.9.9/bin:$PATH

mvn clean verify          # compila + tests + shade + jacoco
mvn test                  # solo tests
mvn -Dtest=PlaneAreaTest test
mvn javafx:run            # UI (requiere pantalla; no funciona en headless/CI)
mvn verify                # informe cobertura: target/site/jacoco/index.html
```

JAR: `target/mathlab-edu-1.0.0-SNAPSHOT-shaded.jar`

## Estructura actual

```
src/main/java/com/mathedu/
   app/            MainApp, AppConfig (DI manual, 100 fórmulas registradas)
  presentation/   navigation/AppCategory, viewmodel/MainViewModel, view/MainController
  application/    calculation/{CalculatorRegistry, CalculationService, FormulaInputFactory}
                  history/{HistoryService, HistoryRepository}
  domain/         shapes/{Shape, PlaneShape, plane/* (15 figuras)}
                  formulas/{FormulaDef, InputDef}
                  calculators/{Calculator, geometry/*, plane/*, algebra/*, trig/*,
                               analytic/*, solid/*, stats/*}
                  algebra/{Fraction, Polynomial, LinearSystems}
                  trig/TriangleSolver
                  engine/ExpressionEngine
                  validation/{Guards, InputParser}
                  history/HistoryEntry
  infrastructure/ persistence/JsonHistoryRepository
  shared/         result/CalculationResult, errors/, units/{LengthUnit, AngleUnit},
                  validation/ValidationResult
src/main/resources/com/mathedu/
  view/MainView.fxml  css/light.css dark.css  i18n/messages_es.properties
```

## Reglas de arquitectura (vigentes)

1. La UI nunca calcula: usa `CalculationService` en `Task`.
2. Nueva fórmula = nuevo `Calculator` + caso en `FormulaInputFactory` + `register()`.
   Sin tocar FXML genérico.
3. Todo error matemático = `CalcException` con mensaje didáctico y campo;
   jamás `NaN/Infinito` crudo.
4. `domain` no importa `javafx` ni `infrastructure`.

Siguiente: **Fase 4** — álgebra lineal (vectores, matrices, determinantes,
inversa, eliminación de Gauss y rango). Cálculo simbólico, integración y series
permanecen en fases posteriores.
