# Añadir una fórmula

[Índice de documentación](../README.md) · [Guías generales](README.md)

El diseño permite añadir operaciones sin modificar el formulario FXML genérico.
El patrón está centralizado en `Calculator`, `FormulaDef`,
`CalculatorRegistry`, `AppConfig` y `FormulaInputFactory`.

## Procedimiento

1. Implementa un `Calculator<T>` dentro del paquete del área en
   `domain/calculators/`. Define un record de entrada si la operación requiere
   más de un valor o una estructura propia.
2. Construye una `FormulaDef` estable: ID único, nombre, categoría, plantilla de
   fórmula, lista ordenada de `InputDef`, etiqueta de resultado y pista de unidad.
3. Implementa `calculate(T)` con validaciones de dominio, errores
   `CalcException`, resultado finito y pasos explicativos en
   `CalculationResult`.
4. Añade el ID a `FormulaInputFactory` para transformar los textos en el record
   tipado. Si la operación acepta un tipo común (como los sólidos), aprovecha el
   patrón existente de despacho por familia.
5. Registra la instancia en `AppConfig` dentro del método correspondiente al
   área. Si necesita motores, inyecta los contratos `ExpressionEngine` o
   `SymbolicEngine` desde esa composición.
6. Si su categoría no existe, actualiza `AppCategory` y comprueba que el nombre
   de categoría coincida con el campo `category` de `FormulaDef`.
7. Añade pruebas unitarias junto al área; cubre éxito, validaciones y errores.
8. Actualiza la página del módulo en
   [`documentacion/modulos/`](../modulos/README.md) y ejecuta `mvn clean verify`.

## Contratos importantes

- Mantén la matemática fuera del controlador y del ViewModel.
- No devuelvas `NaN` o infinito: utiliza las guardas compartidas y errores
  didácticos.
- Declara los nombres de campos coherentes entre `InputDef`, la fábrica de
  entradas y el `field` de `CalcException`.
- Conserva la independencia del dominio respecto a JavaFX e infraestructura.
- Para resultados aproximados, configura `CalculationResult` como aproximado y
  comunica el método/respaldo en sus pasos o nota.

## Navegación del código

- Contrato del calculador: [Calculator.java](../../src/main/java/com/mathedu/domain/calculators/Calculator.java)
- Metadatos: [FormulaDef.java](../../src/main/java/com/mathedu/domain/formulas/FormulaDef.java)
- Registro de dependencias: [AppConfig.java](../../src/main/java/com/mathedu/app/AppConfig.java)
- Construcción de entradas: [FormulaInputFactory.java](../../src/main/java/com/mathedu/application/calculation/FormulaInputFactory.java)
- Validación común: [Guards.java](../../src/main/java/com/mathedu/domain/validation/Guards.java)

Vuelve al [índice principal](../README.md) o consulta la [arquitectura](arquitectura.md).