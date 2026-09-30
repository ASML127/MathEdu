package com.mathedu.presentation.view;

import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.formulas.InputDef;
import com.mathedu.domain.history.HistoryEntry;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.presentation.viewmodel.MainViewModel;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

/**
 * Controlador delgado: bindings + formulario dinamico + historial.
 * PROHIBIDO poner matematicas aqui (regla de arquitectura).
 */
public class MainController {

    @FXML
    private ListView<AppCategory> categoryList;
    @FXML
    private ComboBox<FormulaDef> formulaCombo;
    @FXML
    private Label formulaTemplateLabel;
    @FXML
    private GridPane formGrid;
    @FXML
    private ComboBox<LengthUnit> lengthCombo;
    @FXML
    private ComboBox<AngleUnit> angleCombo;
    @FXML
    private Label angleHintLabel;
    @FXML
    private Button calcButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label resultValueLabel;
    @FXML
    private Label formulaLabel;
    @FXML
    private TextArea stepsArea;
    @FXML
    private CheckBox themeCheck;
    @FXML
    private ListView<HistoryEntry> historyList;
    @FXML
    private Label historyCountLabel;
    @FXML
    private Button favButton;

    private MainViewModel viewModel;

    /** Inyectado por MainApp tras cargar el FXML. */
    public void init(MainViewModel viewModel) {
        this.viewModel = viewModel;

        categoryList.setAccessibleText("Categorías de cálculo");
        categoryList.setAccessibleHelp("Selecciona el área matemática.");
        formulaCombo.setAccessibleText("Fórmula u operación");
        formulaCombo.setAccessibleHelp("Selecciona la operación que deseas calcular.");
        themeCheck.setAccessibleText("Tema oscuro");
        themeCheck.setAccessibleHelp("Activa o desactiva el tema oscuro.");
        calcButton.setAccessibleHelp("Calcula con los valores del formulario.");
        resultValueLabel.setAccessibleText("Resultado del cálculo");
        stepsArea.setAccessibleText("Procedimiento del cálculo");
        historyList.setAccessibleText("Historial de cálculos");

        categoryList.setItems(viewModel.getCategories());
        categoryList.getSelectionModel().select(viewModel.selectedCategoryProperty().get());
        viewModel.selectedCategoryProperty().bind(
                categoryList.getSelectionModel().selectedItemProperty());

        formulaCombo.setItems(viewModel.getVisibleFormulas());
        formulaCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(FormulaDef f) {
                return f == null ? "" : f.displayName();
            }

            @Override
            public FormulaDef fromString(String s) {
                return null;
            }
        });
        formulaCombo.valueProperty().bindBidirectional(viewModel.selectedFormulaProperty());
        if (viewModel.selectedFormulaProperty().get() != null) {
            formulaCombo.getSelectionModel().select(viewModel.selectedFormulaProperty().get());
        }

        lengthCombo.setItems(viewModel.getLengthUnits());
        lengthCombo.valueProperty().bindBidirectional(viewModel.selectedLengthUnitProperty());
        angleCombo.setItems(viewModel.getAngleUnits());
        angleCombo.valueProperty().bindBidirectional(viewModel.selectedAngleUnitProperty());
        angleCombo.visibleProperty().bind(viewModel.angleVisibleProperty());
        angleHintLabel.visibleProperty().bind(viewModel.angleVisibleProperty());

        viewModel.selectedFormulaProperty().addListener((obs, oldV, newV) -> rebuildForm());
        rebuildForm();

        calcButton.disableProperty().bind(viewModel.runningProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
        viewModel.resultProperty().addListener((obs, oldV, newV) -> showResult(newV));

        historyList.setItems(viewModel.getHistoryItems());
        historyList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(HistoryEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                setText(empty || entry == null ? null : entry.displaySummary());
                setWrapText(true);
            }
        });
        viewModel.selectedHistoryProperty().bind(
                historyList.getSelectionModel().selectedItemProperty());
        historyCountLabel.textProperty().bind(
                viewModel.historySizeProperty().asString("Operaciones: %d"));
        viewModel.favoriteStateProperty().addListener((obs, oldV, isFav) ->
                favButton.setText(Boolean.TRUE.equals(isFav) ? "★ Favorita" : "☆ Favorita"));
        favButton.setText(viewModel.favoriteStateProperty().get() ? "★ Favorita" : "☆ Favorita");

        themeCheck.selectedProperty().bindBidirectional(viewModel.darkThemeProperty());
    }

    /** Reconstruye el formulario segun los InputDef de la formula elegida. */
    private void rebuildForm() {
        formGrid.getChildren().clear();
        formGrid.getRowConstraints().clear();
        FormulaDef def = viewModel.selectedFormulaProperty().get();
        if (def == null) {
            formulaTemplateLabel.setText("Modulo disponible en proximas fases.");
            formGrid.add(new Label("Selecciona otra categoria."), 0, 0);
            return;
        }
        formulaTemplateLabel.setText("Formula: " + def.expressionTemplate());
        int row = 0;
        for (InputDef in : def.inputs()) {
            Label name = new Label(in.label() + ":");
            TextField field = new TextField();
            field.setPromptText(in.placeholder());
            name.setLabelFor(field);
            field.setAccessibleText(in.label());
            field.setAccessibleHelp(in.placeholder());
            field.setPrefWidth(160);
            field.setOnAction(event -> viewModel.calculate());
            field.textProperty().bindBidirectional(viewModel.inputProperty(in.key()));
            Label err = new Label();
            err.getStyleClass().add("error");
            err.setWrapText(true);
            err.textProperty().bind(viewModel.fieldErrorProperty(in.key()));
            formGrid.add(name, 0, row);
            formGrid.add(field, 1, row);
            formGrid.add(err, 2, row);
            row++;
        }
        showResult(viewModel.resultProperty().get());
    }

    private void showResult(CalculationResult r) {
        FormulaDef def = viewModel == null ? null : viewModel.selectedFormulaProperty().get();
        if (r == null) {
            resultValueLabel.setText("—");
            formulaLabel.setText(def == null ? "—" : "Formula: " + def.expressionTemplate());
            stepsArea.setText("Introduce los datos y pulsa Calcular.");
            return;
        }
        resultValueLabel.setText("%s %s".formatted(trim(r.value()), r.unitSymbol()));
        formulaLabel.setText("Formula: " + r.formula());
        stepsArea.setText(String.join("\n", r.steps()));
    }

    private static String trim(double v) {
        return String.format("%.6f", v).replaceAll("0+$", "").replaceAll("\\.$", ".0");
    }

    @FXML
    private void onCalculate() {
        viewModel.calculate();
    }

    @FXML
    private void onClear() {
        viewModel.clear();
    }

    @FXML
    private void onCopy() {
        String text = viewModel.copyText();
        if (!text.isBlank()) {
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    @FXML
    private void onRerun() {
        viewModel.rerun(viewModel.selectedHistoryProperty().get());
    }

    @FXML
    private void onToggleFavorite() {
        viewModel.toggleFavoriteSelected();
    }

    @FXML
    private void onRemoveHistory() {
        viewModel.removeSelectedHistory();
    }

    @FXML
    private void onClearHistory() {
        viewModel.clearHistory();
    }
}
