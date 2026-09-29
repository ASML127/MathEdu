package com.mathedu.application.history;

import com.mathedu.domain.history.HistoryEntry;

import java.util.List;
import java.util.Set;

/**
 * Puerto de persistencia del historial (la implementacion JSON vive en
 * {@code infrastructure.persistence}). Permite cambiar a SQLite sin tocar
 * la aplicacion.
 */
public interface HistoryRepository {

    HistoryData load();

    void save(HistoryData data);

    /** Contenedor serializable: entradas + favoritos. */
    record HistoryData(List<HistoryEntry> entries, Set<String> favorites) {
        public HistoryData {
            entries = entries == null ? List.of() : List.copyOf(entries);
            favorites = favorites == null ? Set.of() : Set.copyOf(favorites);
        }

        public static HistoryData empty() {
            return new HistoryData(List.of(), Set.of());
        }
    }
}
