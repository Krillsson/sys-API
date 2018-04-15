package com.krillsson.sysapi.core.monitor;

import java.time.LocalDateTime;

public class MonitorEvent<T> {

    private final LocalDateTime time;
    private final Severity severity;
    private final Type type;
    private final T item;

    public enum Severity {
        NONE,
        WARNING,
        CRITICAL
    }

    public enum Type {
        START,
        ONGOING,
        STOP,
        STANDALONE
    }

    public MonitorEvent(LocalDateTime time, Severity severity, Type type, T item) {
        this.time = time;
        this.severity = severity;
        this.type = type;
        this.item = item;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Type getType() {
        return type;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "MonitorEvent{" +
                "time=" + time +
                ", severity=" + severity +
                ", type=" + type +
                ", item=" + item +
                '}';
    }
}
