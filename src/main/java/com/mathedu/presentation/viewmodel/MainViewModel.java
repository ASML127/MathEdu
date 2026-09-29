package com.mathedu.presentation.viewmodel;

import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.application.history.HistoryService;
import com.mathedu.domain.formulas.FormulaDef;
import com.mathedu.domain.history.HistoryEntry;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.result.CalculationResult;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ViewModel principal (MVVM): estado observable de la UI.
 * Generico en Fase 1: el formulario se construye desde
 * {@link FormulaDef#inputs()} y el input tipado lo crea
 * {@link FormulaInputFactory}. Sin matematicas aqui.
 */
public class MainViewModel {

    private final CalculationService calculationService;
    private final FormulaInputFactory inputFactory;
    private final HistoryService historyService;

    private final ObservableList<AppCategory> categories =
            FXCollections.observableArrayList(AppCategory.values());
    private final ObjectProperty<AppCategory> selectedCategory =
            new SimpleObjectProperty<>(AppCategory.PLANE);

    private final ObservableList<FormulaDef> visibleFormulas = FXCollections.observableArrayList();
    private final ObjectProperty<FormulaDef> selectedFormula = new SimpleObjectProperty<>();

    /** Propiedades de texto por clave de InputDef (orden de la formula). */
    private final Map<String, StringProperty> inputValues = new LinkedHashMap<>();
    /** Errores por campo. */
    private final Map<String, StringProperty> fieldErrors = new LinkedHashMap<>();

    private final ObservableList<LengthUnit> lengthUnits =
            FXCollections.observableArrayList(LengthUnit.values());
    private final ObjectProperty<LengthUnit> selectedLengthUnit =
            new SimpleObjectProperty<>(LengthUnit.CM);
    private final ObservableList<AngleUnit> angleUnits =
            FXCollections.observableArrayList(AngleUnit.values());
    private final ObjectProperty<AngleUnit> selectedAngleUnit =
            new SimpleObjectProperty<>(AngleUnit.DEG);
    private final BooleanProperty angleVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty favoriteState = new SimpleBooleanProperty(false);

    private final ObjectProperty<CalculationResult> result = new SimpleObjectProperty<>();
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private final BooleanProperty darkTheme = new SimpleBooleanProperty(false);

    private final ObservableList<HistoryEntry> historyItems = FXCollections.observableArrayList();
    private final ObjectProperty<HistoryEntry> selectedHistory = new SimpleObjectProperty<>();
    private final IntegerProperty historySize = new SimpleIntegerProperty(0);

    private Task<CalculationResult> currentTask;

    public MainViewModel(CalculationService calculationService,
                         FormulaInputFactory inputFactory,
                         HistoryService historyService) {
        this.calculationService = calculationService;
        this.inputFactory = inputFactory;
        this.historyService = historyService;

        selectedCategory.addListener((obs, oldV, newV) -> filterFormulas());
        selectedFormula.addListener((obs, oldV, newV) -> {
            rebuildInputs();
            updateFavoriteState();
        });

        filterFormulas();
        refreshHistory();
    }

    // ---------- propiedades ----------

    public ObservableList<AppCategory> getCategories() {
        return categories;
    }

    public ObjectProperty<AppCategory> selectedCategoryProperty() {
        return selectedCategory;
    }

    public ObservableList<FormulaDef> getVisibleFormulas() {
        return visibleFormulas;
    }

    public ObjectProperty<FormulaDef> selectedFormulaProperty() {
        return selectedFormula;
    }

    public StringProperty inputProperty(String key) {
        return inputValues.computeIfAbsent(key, k -> new SimpleStringProperty(""));
    }

    public Map<String, StringProperty> inputProperties() {
        return inputValues;
    }

    public StringProperty fieldErrorProperty(String key) {
        return fieldErrors.computeIfAbsent(key, k -> new SimpleStringProperty(""));
    }

    public ObservableList<LengthUnit> getLengthUnits() {
        return lengthUnits;
    }

    public ObjectProperty<LengthUnit> selectedLengthUnitProperty() {
        return selectedLengthUnit;
    }

    public ObservableList<AngleUnit> getAngleUnits() {
        return angleUnits;
    }

    public ObjectProperty<AngleUnit> selectedAngleUnitProperty() {
        return selectedAngleUnit;
    }

    public BooleanProperty angleVisibleProperty() {
        return angleVisible;
    }

    public BooleanProperty favoriteStateProperty() {
        return favoriteState;
    }

    public ObjectProperty<CalculationResult> resultProperty() {
        return result;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    public BooleanProperty darkThemeProperty() {
        return darkTheme;
    }

    public ObservableList<HistoryEntry> getHistoryItems() {
        return historyItems;
    }

    public ObjectProperty<HistoryEntry> selectedHistoryProperty() {
        return selectedHistory;
    }

    public IntegerProperty historySizeProperty() {
        return historySize;
    }

    // ---------- logica ----------

    private void filterFormulas() {
        AppCategory cat = selectedCategory.get();
        String wanted = cat == null ? "" : cat.getDisplayName();
        visibleFormulas.setAll(calculationService.formulas().stream()
            .filter(f -> normalizeCategory(f.category()).equals(normalizeCategory(wanted)))
                .toList());
        if (!visibleFormulas.isEmpty()) {
            FormulaDef current = selectedFormula.get();
            if (current == null || !visibleFormulas.contains(current)) {
                selectedFormula.set(visibleFormulas.get(0));
            } else {
                rebuildInputs();
            }
        } else {
            selectedFormula.set(null);
            inputValues.clear();
            fieldErrors.clear();
        }
    }

    private static String normalizeCategory(String category) {
        return Normalizer.normalize(category, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    private void rebuildInputs() {
        inputValues.clear();
        fieldErrors.clear();
        FormulaDef def = selectedFormula.get();
        if (def == null) {
            angleVisible.set(false);
            return;
        }
        def.inputs().forEach(in -> {
            inputValues.put(in.key(), new SimpleStringProperty(""));
            fieldErrors.put(in.key(), new SimpleStringProperty(""));
        });
        angleVisible.set(def.inputs().stream()
                .anyMatch(i -> i.key().equals("angle") || i.key().startsWith("angle")));
    }

    private void updateFavoriteState() {
        FormulaDef def = selectedFormula.get();
        favoriteState.set(def != null && historyService.isFavorite(def.id()));
    }

    Map<String, String> rawInputs() {
        Map<String, String> raw = new LinkedHashMap<>();
        inputValues.forEach((k, prop) -> raw.put(k, prop.get()));
        return raw;
    }

    /**
     * Parsea y construye el input tipado; vuelca errores por campo.
     * Testeable sin hilos.
     */
    Object validateAndBuild() throws CalcException {
        fieldErrors.values().forEach(p -> p.set(""));
        errorMessage.set("");
        FormulaDef def = selectedFormula.get();
        if (def == null) {
            throw new CalcException(
                    com.mathedu.shared.errors.ErrorCode.UNKNOWN_FORMULA,
                    "Modulo aun no disponible en esta fase.");
        }
        try {
            return inputFactory.buildInput(def.id(), rawInputs(),
                    selectedLengthUnit.get(), selectedAngleUnit.get());
        } catch (CalcException e) {
            if (e.getField() != null && fieldErrors.containsKey(e.getField())) {
                fieldErrors.get(e.getField()).set(e.getMessage());
            } else {
                errorMessage.set(e.getMessage());
            }
            throw e;
        }
    }

    /** Calcula en background sin bloquear la UI. */
    public void calculate() {
        errorMessage.set("");
        result.set(null);
        final Object typedInput;
        try {
            typedInput = validateAndBuild();
        } catch (CalcException e) {
            return;
        }
        if (currentTask != null && currentTask.isRunning()) {
            currentTask.cancel();
        }
        FormulaDef def = selectedFormula.get();
        String formulaId = def.id();
        Map<String, String> rawSnapshot = rawInputs();
        LengthUnit lu = selectedLengthUnit.get() == null ? LengthUnit.CM : selectedLengthUnit.get();
        AngleUnit au = selectedAngleUnit.get() == null ? AngleUnit.DEG : selectedAngleUnit.get();

        Task<CalculationResult> task = new Task<>() {
            @Override
            protected CalculationResult call() throws CalcException {
                return calculationService.compute(formulaId, typedInput);
            }
        };
        task.setOnSucceeded(e -> {
            CalculationResult r = task.getValue();
            result.set(r);
            historyService.add(new HistoryEntry(
                    System.currentTimeMillis(),
                    def.category(), def.id(), def.displayName(),
                    rawSnapshot, r.value(), r.unitSymbol(), r.formula(),
                    lu.name(), au.name()));
            refreshHistory();
            running.set(false);
        });
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            if (ex instanceof CalcException ce) {
                if (ce.getField() != null && fieldErrors.containsKey(ce.getField())) {
                    fieldErrors.get(ce.getField()).set(ce.getMessage());
                } else {
                    errorMessage.set(ce.getMessage());
                }
            } else if (ex != null) {
                errorMessage.set("Error inesperado: " + ex.getMessage());
            } else {
                errorMessage.set("Error desconocido durante el calculo.");
            }
            running.set(false);
        });
        task.setOnCancelled(e -> running.set(false));
        currentTask = task;
        running.set(true);
        Thread worker = new Thread(task, "calc-" + formulaId);
        worker.setDaemon(true);
        worker.start();
    }

    public void clear() {
        if (currentTask != null && currentTask.isRunning()) {
            currentTask.cancel();
        }
        inputValues.values().forEach(p -> p.set(""));
        fieldErrors.values().forEach(p -> p.set(""));
        result.set(null);
        errorMessage.set("");
        running.set(false);
    }

    public String copyText() {
        CalculationResult r = result.get();
        return r == null ? "" : r.toDisplayString();
    }

    public void toggleTheme() {
        darkTheme.set(!darkTheme.get());
    }

    /** Carga los datos de una entrada en el formulario para recalcular. */
    public void rerun(HistoryEntry entry) {
        if (entry == null) {
            return;
        }
        visibleFormulas.stream()
                .filter(f -> f.id().equals(entry.formulaId()))
                .findFirst()
                .ifPresentOrElse(formula -> {
                    selectedFormula.set(formula);
                    // rebuildInputs() ya corrio via listener; rellenar valores:
                    entry.inputs().forEach((k, v) -> {
                        if (inputValues.containsKey(k)) {
                            inputValues.get(k).set(v);
                        }
                    });
                    try {
                        selectedLengthUnit.set(LengthUnit.valueOf(entry.lengthUnit()));
                    } catch (IllegalArgumentException ignored) {
                        selectedLengthUnit.set(LengthUnit.CM);
                    }
                    try {
                        selectedAngleUnit.set(AngleUnit.valueOf(entry.angleUnit()));
                    } catch (IllegalArgumentException ignored) {
                        selectedAngleUnit.set(AngleUnit.DEG);
                    }
                }, () -> errorMessage.set("Esa formula ya no esta disponible."));
    }

    public void toggleFavoriteSelected() {
        FormulaDef def = selectedFormula.get();
        if (def == null) {
            return;
        }
        favoriteState.set(historyService.toggleFavorite(def.id()));
    }

    public void removeSelectedHistory() {
        HistoryEntry entry = selectedHistory.get();
        if (entry != null) {
            historyService.remove(entry);
            refreshHistory();
        }
    }

    public void clearHistory() {
        historyService.clear();
        refreshHistory();
    }

    private void refreshHistory() {
        historyItems.setAll(historyService.entries());
        historySize.set(historyItems.size());
    }
}
