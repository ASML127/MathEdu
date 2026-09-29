# MathLab Edu — Fase 5 implementada

Calculadora y asistente de estudio matemático (Java 21 + JavaFX + Maven).

La [documentación completa del proyecto](documentacion/README.md) incluye guías de
arquitectura, instalación, pruebas, módulos matemáticos y componentes internos.

## Estado

- [x] Fase 0: bootstrap, arquitectura, círculo demo
- [x] Fase 1: geometría plana, unidades de longitud, formulario dinámico e historial JSON
- [x] Fase 2: aritmética, álgebra, polinomios, ecuaciones y trigonometría
- [x] Fase 3: geometría analítica, sólidos 3D y estadística
- [x] Fase 4: vectores y matrices con Apache Commons Math
- [x] Fase 5: cálculo diferencial/integral, series y sucesiones
- [x] 121 fórmulas registradas, incluyendo 1 diferencial, 2 integrales y 8 de series
- [x] Formulario dinámico genérico desde `FormulaDef` (añadir fórmula = 1 clase + `register()`)
- [x] Validación centralizada: `Guards` (dominio) + `InputParser` (texto UI, coma decimal)
- [x] Unidades: `LengthUnit` (mm/cm/m/km/in/ft) + `AngleUnit` (grados/radianes)
- [x] Historial JSON `~/.mathlab-edu/history.json`: re-ejecutar, favoritos, quitar, limpiar
- [x] 116 pruebas automatizadas en verde, incluidas las del motor simbólico real (Symja)

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

### Álgebra lineal

Suma, resta y producto escalar de vectores; suma, resta y producto de matrices;
determinante, inversa, resolución de sistemas cuadrados por eliminación de
Gauss con pivoteo parcial y rango. Introduce las matrices por filas separadas
con punto y coma, y componentes separadas por comas o espacios (por ejemplo,
`1,2;3,4`). Las operaciones incompatibles con las dimensiones de entrada y las
matrices singulares muestran errores didácticos.

### Cálculo diferencial e integral

Derivada simbólica de orden 1–10 evaluada en un punto, integral indefinida
evaluada con la constante fijada en cero para el fallback, e integral definida.
Symja se carga de forma diferida, valida expresiones y limita cada operación a
3 segundos; el arranque en frío de Symja (~2 s de carga de clases) se paga una
sola vez y fuera de ese plazo, para que la primera operación del usuario no
agote el tiempo. Los límites se exactizan con `Rationalize` antes de integrar o
derivar un límite, de modo que el resultado conserva 20 dígitos significativos en
lugar de los 6 que devolvería la aritmética de máquina. Si una integral no puede
resolverse simbólicamente, se intenta cuadratura Gauss-Legendre y luego Simpson; el
resultado se marca como aproximado. La antiderivada numérica se ancla como `F(0)=0`
y también se marca aproximada.

### Series y sucesiones

Sucesiones aritméticas y geométricas con término n y suma parcial, serie
geométrica infinita, sumas finitas de expresiones, criterios de razón y raíz,
comparación por límite para términos no negativos y límites simples con Symja.
Los criterios de razón/raíz informan “inconcluso” cuando `L=1`. Si Symja no
resuelve un límite indeterminado, se aproxima bilateralmente con el motor
numérico y el resultado se marca como aproximado.

Figuras: cuadrado, rectángulo, triángulo (base·altura / Herón / perímetro),
equilátero, isósceles, triángulo rectángulo, paralelogramo, rombo, trapecio, cometa,
círculo, semicírculo, sector (área + arco), corona, polígono regular.

## Requisitos

- JDK 25 (OpenJDK; en Ubuntu, `sudo apt install openjdk-25-jdk`). `JAVA_HOME` debe apuntar a la instalación que contiene `bin/java` y `bin/javac`.
- Maven 3.9+ en el `PATH` del sistema.
- El proyecto se compila con JDK 25, pero fija `maven.compiler.release=21` para generar bytecode compatible con Java 21.

> En Bash, `~/.bashrc` define `JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64` y añade su `bin` al `PATH`.
> Abre una terminal nueva y verifica que `javac -version` funcione; si no, instala el paquete JDK completo, no solo el runtime.

## Comandos

```bash
java -version
javac -version
mvn -version
mvn clean verify          # compila con el JDK activo + tests + shade + jacoco
mvn test                  # solo tests
mvn -Dtest=PlaneAreaTest test
mvn javafx:run            # UI (requiere pantalla; no funciona en headless/CI)
mvn verify                # informe cobertura: target/site/jacoco/index.html
```

JAR: `target/mathlab-edu-1.0.0-SNAPSHOT-shaded.jar`

```bash
mvn package
java -jar target/mathlab-edu-1.0.0-SNAPSHOT-shaded.jar
```

### Por qué «JavaFX runtime components are missing»

Ese error lo lanza la propia JDK, no el proyecto: cuando la clase principal de un
`java -jar` extiende `javafx.application.Application`, la JVM exige que JavaFX esté
en el *module path*, y un JAR con todo en el classpath no cumple. Hay dos salidas
y el proyecto usa las dos:

- `mvn javafx:run` y la configuración `.vscode/launch.json` añaden
  `--module-path` y `--add-modules javafx.controls,javafx.fxml`.
- El JAR sombreado se lanza con `com.mathedu.app.Launcher`, que **no** extiende
  `Application` y delega en `MainApp`, de modo que JavaFX se resuelve desde el
  classpath.

Además, `slf4j-api` queda fijado a 2.x en `dependencyManagement`: Symja arrastra
1.7.x, y con esa versión logback 1.5.8 no se registra y el JAR arranca sin log.

## Estructura actual

```
src/main/java/com/mathedu/
   app/            MainApp, Launcher, AppConfig (DI manual, 121 fórmulas registradas)
  presentation/   navigation/AppCategory, viewmodel/MainViewModel, view/MainController
  application/    calculation/{CalculatorRegistry, CalculationService, FormulaInputFactory}
                  history/{HistoryService, HistoryRepository}
  domain/         shapes/{Shape, PlaneShape, plane/* (15 figuras)}
                  formulas/{FormulaDef, InputDef}
                  calculators/{Calculator, geometry/*, plane/*, algebra/*, trig/*,
                               analytic/*, solid/*, stats/*, linear/*, calculus/*, series/*}
                  algebra/{Fraction, Polynomial, LinearSystems}
                  trig/TriangleSolver
                  engine/{ExpressionEngine, SymbolicEngine}
                  validation/{Guards, InputParser}
                  history/HistoryEntry
  infrastructure/ persistence/JsonHistoryRepository
                  engine/EvalExEngine
                  symbolic/{LazySymbolicEngine, SymjaSymbolicEngine}
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

Siguiente: **Fase 6** — pulido de UI, accesibilidad, pruebas de humo y empaquetado
multiplataforma con `jlink`/`jpackage`.
