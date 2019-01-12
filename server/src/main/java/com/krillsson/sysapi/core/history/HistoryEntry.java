package com.krillsson.sysapi.core.history;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class HistoryEntry<T> {
    public final ZonedDateTime date;
    public final T value;

    public HistoryEntry(ZonedDateTime date, T value) {
        this.date = date;
        this.value = value;
    }
}
