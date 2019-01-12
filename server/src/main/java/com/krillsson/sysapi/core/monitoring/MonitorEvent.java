package com.krillsson.sysapi.core.monitoring;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class MonitorEvent {
    private final UUID id;
    private final ZonedDateTime time;
    private final String monitorId;
    private final MonitorStatus monitorStatus;
    private final Monitor.MonitorType monitorType;
    private final double threshold;
    private final double value;

    public enum MonitorStatus {
        START,
        STOP,
    }

    public MonitorEvent(UUID id, String monitorId, ZonedDateTime time, MonitorStatus monitorStatus, Monitor.MonitorType monitorType, Double threshold, Double value) {
        this.id = id;
        this.time = time;
        this.monitorId = monitorId;
        this.monitorStatus = monitorStatus;
        this.monitorType = monitorType;
        this.threshold = threshold;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public MonitorStatus getMonitorStatus() {
        return monitorStatus;
    }

    public Monitor.MonitorType getMonitorType() {
        return monitorType;
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
                ", id='" + monitorId + '\'' +
                ", monitorStatus=" + monitorStatus +
                ", threshold=" + threshold +
                ", value=" + value +
                '}';
    }
}
