package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.util.TimeMachine;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class History<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(History.class);

    private final List<HistoryEntry<T>> history;
    private final TimeMachine timeMachine;

    protected History() {
        this(new TimeMachine());
    }

    protected History(TimeMachine timeMachine) {
        this.timeMachine = timeMachine;
        this.history = new ArrayList<>();
    }

    public List<HistoryEntry<T>> get() {
        return Collections.unmodifiableList(history);
    }

    public void record(T value) {
        LOGGER.trace("Recording history for {}", value.getClass().getSimpleName());
        history.add(new HistoryEntry<>(timeMachine.now(/* with system timezone */), value));
    }

    public void purge(int olderThan, ChronoUnit unit) {
        LocalDateTime maxAge = timeMachine.now().minus(olderThan, unit);
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
