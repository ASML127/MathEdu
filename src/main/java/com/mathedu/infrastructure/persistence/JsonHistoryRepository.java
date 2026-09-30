package com.mathedu.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mathedu.application.history.HistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Guarda el historial en un JSON local: {@code ~/.mathedu/history.json}.
 * Sin drivers ni SQL (YAGNI en Fase 1). Escritura atomica via fichero temporal.
 */
public class JsonHistoryRepository implements HistoryRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonHistoryRepository.class);

    private final Path file;
    private final ObjectMapper mapper;

    public JsonHistoryRepository(Path file) {
        this.file = file;
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** Ruta por defecto del historial de usuario. */
    public static Path defaultPath() {
        return Path.of(System.getProperty("user.home"), ".mathedu", "history.json");
    }

    @Override
    public HistoryData load() {
        if (!Files.isRegularFile(file)) {
            return HistoryData.empty();
        }
        try {
            HistoryData data = mapper.readValue(file.toFile(), HistoryData.class);
            return data == null ? HistoryData.empty() : data;
        } catch (IOException e) {
            log.warn("Historial corrupto o incompatible ({}): se empieza vacio.", file, e);
            return HistoryData.empty();
        }
    }

    @Override
    public void save(HistoryData data) {
        try {
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            mapper.writeValue(tmp.toFile(), data);
            Files.move(tmp, file,
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            log.warn("No se pudo guardar el historial en {}.", file, e);
        }
    }
}
