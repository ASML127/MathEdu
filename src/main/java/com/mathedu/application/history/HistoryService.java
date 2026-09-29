package com.mathedu.application.history;

import com.mathedu.domain.history.HistoryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Historial en memoria (max 500) con persistencia delegada.
 * La mas reciente primero. Thread-safe basico para uso desde Tasks.
 */
public class HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);

    public static final int MAX_ENTRIES = 500;

    private final HistoryRepository repository;
    private final Deque<HistoryEntry> entries = new ArrayDeque<>();
    private final Set<String> favorites = new LinkedHashSet<>();

    public HistoryService(HistoryRepository repository) {
        this.repository = repository;
        HistoryRepository.HistoryData data = repository.load();
        data.entries().stream()
                .sorted((a, b) -> Long.compare(a.epochMillis(), b.epochMillis()))
                .forEach(entries::addLast);
        while (entries.size() > MAX_ENTRIES) {
            entries.removeFirst();
        }
        favorites.addAll(data.favorites());
        log.debug("Historial cargado: {} entradas, {} favoritas.",
                entries.size(), favorites.size());
    }

    public synchronized void add(HistoryEntry entry) {
        entries.addFirst(entry);
        while (entries.size() > MAX_ENTRIES) {
            entries.removeLast();
        }
        persist();
    }

    /** Mas recientes primero. */
    public synchronized List<HistoryEntry> entries() {
        return List.copyOf(entries);
    }

    public synchronized void remove(HistoryEntry entry) {
        entries.remove(entry);
        persist();
    }

    public synchronized void clear() {
        entries.clear();
        persist();
    }

    /** Alterna favorito; devuelve el nuevo estado. */
    public synchronized boolean toggleFavorite(String formulaId) {
        boolean now;
        if (favorites.contains(formulaId)) {
            favorites.remove(formulaId);
            now = false;
        } else {
            favorites.add(formulaId);
            now = true;
        }
        persist();
        return now;
    }

    public synchronized boolean isFavorite(String formulaId) {
        return favorites.contains(formulaId);
    }

    public synchronized Set<String> favorites() {
        return Set.copyOf(favorites);
    }

    private void persist() {
        repository.save(new HistoryRepository.HistoryData(entries(), favorites()));
    }
}
