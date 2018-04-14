package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.config.HistoryPurgingConfiguration;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;

public abstract class History<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(History.class);

    private final Map<LocalDateTime, T> history;
    private final HistoryPurgingConfiguration configuration;

    protected History(HistoryPurgingConfiguration configuration) {
        this.configuration = configuration;
        history = new LinkedHashMap<>();
    }

    public Map<LocalDateTime, T> get() {
        return Collections.unmodifiableMap(history);
    }

    public void record() {
        T value = getCurrent().get();
        LOGGER.trace("Recording history for {}", value.getClass().getSimpleName());
        history.put(LocalDateTime.now(/* with system timezone */), value);
    }

    public void purge() {
        purge(configuration.getOlderThan(), configuration.getUnit());
    }

    public void purge(int olderThan, ChronoUnit unit) {
        LocalDateTime maxAge = LocalDateTime.now().minus(olderThan, unit);
        Set<LocalDateTime> toBeRemoved = new HashSet<>();
        for (LocalDateTime historyEntry : history.keySet()) {
            if (historyEntry.isBefore(maxAge)) {
                toBeRemoved.add(historyEntry);
            }
        }

        LOGGER.trace("Purging {} entries older than {} {}", toBeRemoved.size(), olderThan, unit.name());
        toBeRemoved.forEach(history::remove);
    }

    abstract Supplier<T> getCurrent();
}
