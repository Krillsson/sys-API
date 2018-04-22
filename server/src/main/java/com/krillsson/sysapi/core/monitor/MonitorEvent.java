package com.krillsson.sysapi.core.monitor;

import java.time.LocalDateTime;

public class MonitorEvent {
    private final LocalDateTime time;
    private final Severity severity;
    private final String id;
    private final Type type;
    private final double threshold;
    private final double value;

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

    public MonitorEvent(LocalDateTime time, String id, Severity severity, Type type, Double threshold, Double value) {
        this.time = time;
        this.id = id;
        this.severity = severity;
        this.type = type;
        this.threshold = threshold;
        this.value = value;
    }

    public String getId() {
        return id;
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

    public double getThreshold() {
        return threshold;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MonitorEvent{" +
                "time=" + time +
                ", severity=" + severity +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
