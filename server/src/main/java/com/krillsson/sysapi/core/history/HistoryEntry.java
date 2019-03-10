package com.krillsson.sysapi.core.history;

import java.time.OffsetDateTime;

public class HistoryEntry<T> {
    public final OffsetDateTime date;
    public final T value;

    public HistoryEntry(OffsetDateTime date, T value) {
        this.date = date;
        this.value = value;
    }
}
