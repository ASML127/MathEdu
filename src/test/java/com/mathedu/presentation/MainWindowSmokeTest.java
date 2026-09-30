package com.mathedu.presentation;

import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.CalculatorRegistry;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.application.history.HistoryService;
import com.mathedu.app.AppConfig;
import com.mathedu.infrastructure.persistence.JsonHistoryRepository;
import com.mathedu.presentation.view.MainController;
import com.mathedu.presentation.viewmodel.MainViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
@EnabledIfSystemProperty(named = "phase6.ui", matches = "true")
class MainWindowSmokeTest {

    @TempDir
    Path temp;

    private MainViewModel viewModel;
    private Scene scene;

    @Start
    void start(Stage stage) throws Exception {
        CalculatorRegistry registry = AppConfig.buildRegistry();
        HistoryService history = new HistoryService(
                new JsonHistoryRepository(temp.resolve("history.json")));
        viewModel = new MainViewModel(new CalculationService(registry),
                new FormulaInputFactory(), history);

        FXMLLoader loader = new FXMLLoader(
                MainWindowSmokeTest.class.getResource("/com/mathedu/view/MainView.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.init(viewModel);

        scene = new Scene(root, 1150, 680);
        String light = MainWindowSmokeTest.class
                .getResource("/com/mathedu/css/light.css").toExternalForm();
        String dark = MainWindowSmokeTest.class
                .getResource("/com/mathedu/css/dark.css").toExternalForm();
        scene.getStylesheets().add(light);
        viewModel.darkThemeProperty().addListener((obs, oldValue, isDark) -> {
            scene.getStylesheets().setAll(isDark ? dark : light);
        });
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void loadsAccessibleFormCalculatesAndSwitchesThemeFromKeyboard(FxRobot robot) throws Exception {
        CountDownLatch resultReady = new CountDownLatch(1);
        AtomicReference<String> calculationError = new AtomicReference<>();
        viewModel.resultProperty().addListener((obs, oldValue, result) -> {
            if (result != null) {
                resultReady.countDown();
            }
        });
        viewModel.errorMessageProperty().addListener((obs, oldValue, message) -> {
            if (message != null && !message.isBlank()) {
                calculationError.set(message);
                resultReady.countDown();
            }
        });

        TextField side = robot.lookup(".text-field").queryAs(TextField.class);
        Button calculate = robot.lookup("#calcButton").queryAs(Button.class);
        robot.interact(() -> {
            side.setText("4");
            calculate.fire();
        });
        assertThat(resultReady.await(5, TimeUnit.SECONDS))
            .as("Calculation did not finish; error: %s", calculationError.get()).isTrue();
        WaitForAsyncUtils.waitForFxEvents();

        AtomicReference<String> resultText = new AtomicReference<>();
        AtomicReference<String> inputAccessibleName = new AtomicReference<>();
        robot.interact(() -> {
            resultText.set(robot.lookup("#resultValueLabel").queryAs(Label.class).getText());
            inputAccessibleName.set(side.getAccessibleText());
        });
        assertThat(resultText.get()).isEqualTo("16.0 cm^2");
        assertThat(inputAccessibleName.get()).isEqualTo("Lado (l)");

        robot.clickOn("#themeCheck");
        AtomicReference<Boolean> darkStylesheetLoaded = new AtomicReference<>(false);
        robot.interact(() -> darkStylesheetLoaded.set(scene.getStylesheets().stream()
                .anyMatch(stylesheet -> stylesheet.endsWith("dark.css"))));
        assertThat(darkStylesheetLoaded.get()).isTrue();

        robot.press(KeyCode.SPACE).release(KeyCode.SPACE);
        AtomicReference<Boolean> lightStylesheetLoaded = new AtomicReference<>(false);
        robot.interact(() -> lightStylesheetLoaded.set(scene.getStylesheets().stream()
            .anyMatch(stylesheet -> stylesheet.endsWith("light.css"))));
        assertThat(lightStylesheetLoaded.get()).isTrue();
    }
}