package com.mathedu.presentation.viewmodel;

import com.mathedu.app.AppConfig;
import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.CalculatorRegistry;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.application.history.HistoryRepository;
import com.mathedu.application.history.HistoryService;
import com.mathedu.domain.calculators.plane.SquareAreaCalculator;
import com.mathedu.presentation.navigation.AppCategory;
import com.mathedu.infrastructure.persistence.JsonHistoryRepository;
import com.mathedu.shared.errors.CalcException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * ViewModel sin hilos: validacion, mapeo de errores por campo
 * y filtrado de formulas (headless, solo javafx.base).
 */
class MainViewModelTest {

    @TempDir
    Path temp;

    private MainViewModel viewModel() {
        CalculatorRegistry registry = AppConfig.buildRegistry();
        HistoryRepository repo = new JsonHistoryRepository(temp.resolve("h.json"));
        return new MainViewModel(new CalculationService(registry),
                new FormulaInputFactory(), new HistoryService(repo));
    }

    @Test
    void formulasFiltradasPorCategoria() {
        MainViewModel vm = viewModel();
        // 31 (plane) + 28 (algebra) + 13 (trig) + 10 (analytic)
        // + 10 (solids) + 8 (statistics) = 100.
        // Categoria predeterminada: PLANE.
        assertThat(vm.getVisibleFormulas()).hasSize(31);
        assertThat(vm.selectedFormulaProperty().get().id())
                .isEqualTo(SquareAreaCalculator.FORMULA_ID);
    }

    @Test
    void entradaValidaConstruyeInputTipado() throws CalcException {
        MainViewModel vm = viewModel();
        vm.inputProperty("side").set("4");
        Object in = vm.validateAndBuild();
        assertThat(in).isInstanceOf(SquareAreaCalculator.Input.class);
    }

    @Test
    void entradaInvalidaMarcaErrorDeCampo() {
        MainViewModel vm = viewModel();
        vm.inputProperty("side").set("");
        assertThatThrownBy(vm::validateAndBuild).isInstanceOf(CalcException.class);
        assertThat(vm.fieldErrorProperty("side").get()).isNotBlank();
    }

    @Test
    void textoMalformadoMarcaErrorDeCampo() {
        MainViewModel vm = viewModel();
        vm.inputProperty("side").set("xyz");
        assertThatThrownBy(vm::validateAndBuild).isInstanceOf(CalcException.class);
        assertThat(vm.fieldErrorProperty("side").get()).contains("xyz");
    }

    @Test
    void cambiarFormulaReconstruyeInputs() {
        MainViewModel vm = viewModel();
        vm.inputProperty("side").set("4");
        vm.selectedFormulaProperty().set(
                vm.getVisibleFormulas().stream()
                        .filter(f -> f.id().equals("plane.rectangle.area"))
                        .findFirst().orElseThrow());
        assertThat(vm.inputProperties().keySet()).containsExactlyInAnyOrder("width", "height");
    }

    @Test
    void registryContieneLas100Formulas() {
        CalculatorRegistry registry = AppConfig.buildRegistry();
        List<String> ids = registry.all().stream().map(c -> c.formula().id()).toList();
        assertThat(ids).hasSize(100).doesNotHaveDuplicates();
    }

    @Test
    void categoriaAlgebraMuestraFactorizacion() {
        MainViewModel vm = viewModel();
        vm.selectedCategoryProperty().set(AppCategory.ALGEBRA);

        assertThat(vm.getVisibleFormulas()).hasSize(28)
                .anyMatch(formula -> formula.id().equals("arith.poly-factor"));
    }
}
