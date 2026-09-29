package com.mathedu.application.history;

import com.mathedu.domain.history.HistoryEntry;
import com.mathedu.infrastructure.persistence.JsonHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/** Historial: orden, limite, favoritos y persistencia JSON. */
class HistoryServiceTest {

    @TempDir
    Path temp;

    private HistoryService service() {
        return new HistoryService(new JsonHistoryRepository(temp.resolve("history.json")));
    }

    private static HistoryEntry entry(String formulaId, double value) {
        return new HistoryEntry(System.currentTimeMillis(), "Geometria plana", formulaId,
                "Prueba", Map.of("r", "3"), value, "cm^2", "A = pi*r^2", "CM", "DEG");
    }

    @Test
    void masRecientePrimero() throws InterruptedException {
        HistoryService s = service();
        s.add(entry("a", 1));
        Thread.sleep(2);
        s.add(entry("b", 2));
        assertThat(s.entries()).hasSize(2);
        assertThat(s.entries().get(0).formulaId()).isEqualTo("b");
    }

    @Test
    void favoritosAlternan() {
        HistoryService s = service();
        assertThat(s.isFavorite("plane.square.area")).isFalse();
        assertThat(s.toggleFavorite("plane.square.area")).isTrue();
        assertThat(s.isFavorite("plane.square.area")).isTrue();
        assertThat(s.toggleFavorite("plane.square.area")).isFalse();
    }

    @Test
    void persisteTrasReinicio() {
        HistoryService s = service();
        s.add(entry("plane.circle.area", 28.27));
        s.toggleFavorite("plane.circle.area");

        HistoryService reloaded = service();
        assertThat(reloaded.entries()).hasSize(1);
        assertThat(reloaded.entries().get(0).resultValue()).isEqualTo(28.27);
        assertThat(reloaded.isFavorite("plane.circle.area")).isTrue();
    }

    @Test
    void limpiarVacía() {
        HistoryService s = service();
        s.add(entry("a", 1));
        s.clear();
        assertThat(s.entries()).isEmpty();
    }

    @Test
    void respetaLimiteMaximo() {
        HistoryService s = service();
        for (int i = 0; i < HistoryService.MAX_ENTRIES + 10; i++) {
            s.add(entry("f" + i, i));
        }
        assertThat(s.entries()).hasSize(HistoryService.MAX_ENTRIES);
        assertThat(s.entries().get(0).formulaId())
                .isEqualTo("f" + (HistoryService.MAX_ENTRIES + 9));
    }
}
