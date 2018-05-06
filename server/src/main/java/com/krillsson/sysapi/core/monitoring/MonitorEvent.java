package com.krillsson.sysapi.core.monitoring;


import java.time.LocalDateTime;

public class MonitorEvent {
    private final LocalDateTime time;
    private final String id;
    private final MonitorStatus monitorStatus;
    private final Monitor.MonitorType monitorType;
    private final double threshold;
    private final double value;

    public enum MonitorStatus {
        START,
        STOP,
    }

    public MonitorEvent(LocalDateTime time, String id, MonitorStatus monitorStatus, Monitor.MonitorType monitorType, Double threshold, Double value) {
        this.time = time;
        this.id = id;
        this.monitorStatus = monitorStatus;
        this.monitorType = monitorType;
        this.threshold = threshold;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTime() {
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
                ", id='" + id + '\'' +
                ", monitorStatus=" + monitorStatus +
                ", threshold=" + threshold +
                ", value=" + value +
                '}';
    }
}
