package com.krillsson.sysapi.dto.monitor;

public class MonitorEvent {

    private final String id;
    private final String monitorId;
    private final String time;
    private final MonitorType type;
    private final MonitorStatus monitorStatus;
    private final double threshold;
    private final double value;

    public MonitorEvent(String id, String monitorId, String time, MonitorStatus monitorStatus, MonitorType type, Double threshold, Double value) {
        this.id = id;
        this.time = time;
        this.monitorId = monitorId;
        this.type = type;
        this.monitorStatus = monitorStatus;
        this.threshold = threshold;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public String getTime() {
        return time;
    }

    public MonitorStatus getMonitorStatus() {
        return monitorStatus;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getValue() {
        return value;
    }

    public MonitorType getType() {
        return type;
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
