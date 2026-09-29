# Arquitectura

[Índice de documentación](../README.md) · [Guías generales](README.md)

## Estilo y límites

El proyecto usa una separación por capas con inspiración en arquitectura limpia,
MVVM para la presentación y composición manual de dependencias. No hay contenedor
de inyección de dependencias: [AppConfig](../../src/main/java/com/mathedu/app/AppConfig.java)
ensambla los servicios y registra los calculadores.

```text
JavaFX / FXML
  MainController -> MainViewModel
                       |             \
                       v              v
              CalculationService   HistoryService
                       |              |
                       v              v
              CalculatorRegistry   HistoryRepository
                       |              ^
                       v              |
                 domain calculators  JsonHistoryRepository
                       |
            ExpressionEngine / SymbolicEngine
                       ^
                       |
              EvalExEngine / LazySymbolicEngine
```

La dirección importante es que el dominio define contratos y no depende de
JavaFX ni de las implementaciones de infraestructura. Las interfaces de motor y
repositorio permiten que el dominio/aplicación use abstracciones, mientras que
la infraestructura ofrece adaptadores concretos.

## Capas y responsabilidad

| Capa | Paquetes | Responsabilidad |
|---|---|---|
| Arranque | `com.mathedu.app` | Crear dependencias, cargar FXML/CSS e iniciar JavaFX. |
| Presentación | `presentation.view`, `viewmodel`, `navigation` | Enlazar controles, estado observable, categorías y acciones de usuario. |
| Aplicación | `application.calculation`, `application.history` | Registro/ejecución de casos de uso y gestión del historial. |
| Dominio | `domain.calculators`, `formulas`, `shapes`, `algebra`, `trig`, `validation`, `engine`, `history` | Reglas matemáticas, modelos y contratos independientes de UI. |
| Infraestructura | `infrastructure.engine`, `symbolic`, `persistence` | Adaptadores EvalEx, Symja y Jackson/JSON. |
| Compartida | `shared` | Resultado, errores, validación y unidades usados entre capas. |

Los detalles por paquete están en [componentes técnicos](../componentes/README.md).

## Reglas de dependencia

- La interfaz no implementa cálculos; delega en `CalculationService` desde un
  `javafx.concurrent.Task`.
- Las calculadoras exponen `FormulaDef` y reciben entradas tipadas.
- La presentación conoce metadatos (`FormulaDef`, `InputDef`), no la matemática
  concreta de cada calculadora.
- `domain` no debe importar clases de JavaFX ni de `infrastructure`.
- Errores de entrada/dominio se comunican como `CalcException`, con código y,
  cuando corresponde, clave del campo afectado.
- Resultados visibles se representan con `CalculationResult`, incluyendo pasos,
  unidad y si el valor es exacto o aproximado.

## Ensamblado y extensibilidad

`AppConfig.buildRegistry()` instancia motores y registra los calculadores por
área. `CalculatorRegistry` indexa por `formula().id()`. El formulario es genérico:
la definición de entradas procede de `FormulaDef.inputs()`, y
`FormulaInputFactory` convierte los textos en el record que consume el calculador.

Para ampliar el catálogo, sigue [Añadir una fórmula](como-anadir-formulas.md).
Para ver el catálogo funcional, consulta [Módulos matemáticos](../modulos/README.md).