# Arranque y presentación

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

## Arranque

- `app/MainApp.java` extiende `Application`, crea el registro/servicios, carga
  `MainView.fxml`, conecta el controlador y cambia entre CSS claro y oscuro.
- `app/Launcher.java` sirve como clase principal del JAR sombreado y delega en
  `MainApp`, evitando la limitación de arranque JavaFX descrita en
  [instalación](../../docs/instalacion-y-ejecucion.md).
- `app/AppConfig.java` compone las dependencias sin framework y es el punto
  central de registro de las 121 operaciones.

## Presentación

- `presentation/view/MainController.java` conecta nodos FXML, bindings y
  acciones. Reconstruye los campos desde `FormulaDef.inputs()` y representa
  resultado, pasos, errores e historial. No debe contener matemática.
- `presentation/viewmodel/MainViewModel.java` mantiene propiedades observables,
  filtra fórmulas, valida/crea entradas, lanza `Task`, y gestiona resultados,
  historial y favoritos.
- `presentation/navigation/AppCategory.java` define las categorías del menú.

El patrón y las restricciones de dependencia se explican en [Arquitectura](../../docs/arquitectura.md);
el flujo completo está en [Flujo de cálculo](../../docs/flujo-de-calculo.md).
Prueba relacionada: `MainViewModelTest`.