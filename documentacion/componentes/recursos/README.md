# Interfaz y recursos

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

Los recursos de UI están en `src/main/resources/com/mathedu/`:

- `view/MainView.fxml`: estructura y nodos inyectados en `MainController`.
- `css/light.css` y `css/dark.css`: temas alternables desde el ViewModel.
- `i18n/messages_es.properties`: textos en español.
- `src/main/resources/logback.xml`: configuración de logging.

La vista genera dinámicamente los campos de entrada según los metadatos
`InputDef`; por eso una fórmula nueva normalmente no requiere editar FXML. El
controlador conecta nodos y acciones, y las propiedades del ViewModel contienen
el estado. Cambios a campos FXML deben mantenerse sincronizados con las
referencias `@FXML` de `MainController`.

Para el comportamiento de la pantalla, consulta [Arranque y presentación](../app-y-presentacion/README.md)
y [Flujo de cálculo](../../docs/flujo-de-calculo.md). Para ejecutar la interfaz,
consulta [Instalación](../../docs/instalacion-y-ejecucion.md). Volver al
[índice principal](../../README.md).