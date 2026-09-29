package com.mathedu.app;

import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.CalculatorRegistry;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.application.history.HistoryService;
import com.mathedu.presentation.view.MainController;
import com.mathedu.presentation.viewmodel.MainViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada JavaFX. Solo bootstrap: DI manual, FXML, CSS, tema.
 */
public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage stage) throws Exception {
        CalculatorRegistry registry = AppConfig.buildRegistry();
        CalculationService service = AppConfig.buildCalculationService(registry);
        FormulaInputFactory factory = AppConfig.buildInputFactory();
        HistoryService history = AppConfig.buildHistoryService();
        MainViewModel viewModel = new MainViewModel(service, factory, history);

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/com/mathedu/view/MainView.fxml"));
        javafx.scene.Parent root = loader.load();
        MainController controller = loader.getController();
        controller.init(viewModel);

        Scene scene = new Scene(root, 1150, 680);
        scene.getStylesheets().add(
                MainApp.class.getResource("/com/mathedu/css/light.css").toExternalForm());
        String darkUrl = MainApp.class.getResource("/com/mathedu/css/dark.css").toExternalForm();
        String lightUrl = scene.getStylesheets().get(0);

        viewModel.darkThemeProperty().addListener((obs, oldV, isDark) -> {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(isDark ? darkUrl : lightUrl);
        });

        stage.setTitle(AppConfig.APP_NAME + " — " + AppConfig.APP_VERSION);
        stage.setScene(scene);
        stage.show();
        log.info("MathLab Edu iniciado ({})", AppConfig.APP_VERSION);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
