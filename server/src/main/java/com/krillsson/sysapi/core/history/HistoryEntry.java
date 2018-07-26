package com.krillsson.sysapi.core.history;

import java.time.LocalDateTime;

public class HistoryEntry<T> {
    public final LocalDateTime date;
    public final T value;

    public HistoryEntry(LocalDateTime date, T value) {
        this.date = date;
        this.value = value;
    }
}
