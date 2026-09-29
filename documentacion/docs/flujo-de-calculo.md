# Flujo de cálculo

[Índice de documentación](../README.md) · [Guías generales](README.md)

Una operación sigue este recorrido desde la selección hasta el historial:

1. `MainController` enlaza la selección de categoría y fórmula con el
   `MainViewModel`; el FXML contiene la estructura visual.
2. El `MainViewModel` filtra las definiciones por categoría y reconstruye campos
   a partir de los `InputDef` de la fórmula seleccionada.
3. Al pulsar Calcular, `FormulaInputFactory` parsea los textos, incorpora las
   unidades elegidas y construye el record tipado correspondiente.
4. El modelo crea un `Task` para que el cálculo no bloquee el hilo de JavaFX.
5. `CalculationService.compute(id, input)` obtiene la implementación del
   `CalculatorRegistry`, ejecuta `calculate` y rechaza resultados no finitos.
6. El éxito actualiza el resultado observable y añade una entrada al historial;
   un `CalcException` se muestra junto al campo o como error general.

Las implementaciones principales son
[MainController](../../src/main/java/com/mathedu/presentation/view/MainController.java),
[MainViewModel](../../src/main/java/com/mathedu/presentation/viewmodel/MainViewModel.java),
[FormulaInputFactory](../../src/main/java/com/mathedu/application/calculation/FormulaInputFactory.java),
[CalculationService](../../src/main/java/com/mathedu/application/calculation/CalculationService.java)
y [CalculatorRegistry](../../src/main/java/com/mathedu/application/calculation/CalculatorRegistry.java).

## Validación y errores

El parser transforma la entrada de texto; la validación de dominio comprueba
rangos y restricciones matemáticas. Los errores conservan una clave de campo para
que la interfaz pueda mostrar el mensaje en contexto. El servicio comprueba
además que el resultado principal no sea `NaN` ni infinito.

## Ejecución asíncrona

El cálculo se ejecuta en un hilo daemon con un `Task`. Si se inicia otra operación
mientras una anterior sigue activa, el modelo solicita su cancelación. Los
handlers del `Task` actualizan las propiedades de UI y el historial al finalizar.

## Recorrido alternativo: historial

`HistoryService` carga y limita las entradas, ordena por fecha y administra
favoritos. `JsonHistoryRepository` serializa los datos en un archivo temporal y
lo mueve al destino para reemplazar el JSON. El usuario puede recargar entradas
en el formulario, quitar una entrada o vaciar el historial.

Consulta [historial y persistencia](../componentes/aplicacion/README.md) y
[pruebas](pruebas.md) para las clases que cubren el recorrido.