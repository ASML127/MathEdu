# Instalación y ejecución

[Índice de documentación](../README.md) · [Guías generales](README.md)

## Requisitos

- JDK 25 para compilar y ejecutar el entorno documentado.
- Maven 3.9 o superior en `PATH`.
- El proyecto compila con `maven.compiler.release=21`, por lo que el bytecode
  generado es compatible con Java 21.
- Para abrir la interfaz gráfica se necesita un entorno con pantalla disponible.

La versión exacta de JavaFX y de las dependencias está declarada en
[pom.xml](../../pom.xml). El [README raíz](../../README.md) explica la configuración
de `JAVA_HOME` usada en el entorno de desarrollo actual.

## Compilar y probar

Desde la raíz del repositorio:

```bash
mvn clean verify
```

El comando limpia, compila, ejecuta pruebas, empaqueta el artefacto sombreado y
genera el informe JaCoCo en `target/site/jacoco/index.html`.

```bash
mvn test
mvn -Dtest=PlaneAreaTest test
```

El primer comando ejecuta las pruebas sin fases posteriores de empaquetado; el
segundo ejecuta una clase de prueba concreta.

## Iniciar la aplicación

```bash
mvn javafx:run
```

Este objetivo configura JavaFX para el arranque Maven. No es apropiado para CI
headless si la prueba intenta abrir una ventana.

## JAR sombreado

```bash
mvn package
java -jar target/mathedu-1.0.0-shaded.jar
```

El manifiesto del JAR usa `com.mathedu.app.Launcher`, que delega en `MainApp`.
Este lanzador evita la comprobación especial de la JDK que exige JavaFX en
`module-path` cuando la clase principal extiende `Application`. El objetivo
`javafx:run` usa directamente `MainApp`.

## Instaladores nativos

Los releases estables adjuntan un app-image y paquete `.deb` para Linux, y un
instalador `.msi` para Windows, todos con runtime incluido. Descarga el paquete
de tu sistema desde GitHub Releases. macOS queda fuera de la versión 1.0.0.

## Configuración y datos de usuario

El historial se guarda en `~/.mathedu/history.json`. No se configura mediante
un servidor ni una base de datos. Los registros del proceso usan SLF4J con
Logback; la configuración está en `src/main/resources/logback.xml`.

Para el funcionamiento matemático y sus motores consulta [Motores e infraestructura](../componentes/infraestructura/README.md),
y para persistencia, [historial](../componentes/aplicacion/README.md).