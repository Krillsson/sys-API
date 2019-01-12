package com.krillsson.sysapi.core.history;

import com.google.common.annotations.VisibleForTesting;
import com.krillsson.sysapi.util.Clock;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class History<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(History.class);

    private final List<HistoryEntry<T>> history;
    private final Clock clock;

    protected History() {
        this(new Clock());
    }

    @VisibleForTesting
    protected History(Clock clock) {
        this.clock = clock;
        this.history = new ArrayList<>();
    }

    public List<HistoryEntry<T>> get() {
        return Collections.unmodifiableList(history);
    }

    public void record(T value) {
        LOGGER.trace("Recording history for {}", value.getClass().getSimpleName());
        history.add(new HistoryEntry<>(clock.now(/* with system timezone */), value));
    }

    public void purge(int olderThan, ChronoUnit unit) {
        ZonedDateTime maxAge = clock.now().minus(olderThan, unit);
        Set<HistoryEntry> toBeRemoved = new HashSet<>();
        for (HistoryEntry historyEntry : history) {
            if (historyEntry.date.isBefore(maxAge)) {
                toBeRemoved.add(historyEntry);
            }
        }

        LOGGER.trace("Purging {} entries older than {} {}", toBeRemoved.size(), olderThan, unit.name());
        toBeRemoved.forEach(history::remove);
    }
}
