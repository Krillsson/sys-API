package com.krillsson.sysapi.dto.monitor;

import java.util.Date;

public class MonitorEvent {

    private final String id;
    private final String itemId;
    private final Date time;
    private final MonitorType type;
    private final MonitorStatus monitorStatus;
    private final double threshold;
    private final double value;

    public MonitorEvent(String id, String itemId, Date time, MonitorStatus monitorStatus, MonitorType type, Double threshold, Double value) {
        this.id = id;
        this.time = time;
        this.itemId = itemId;
        this.type = type;
        this.monitorStatus = monitorStatus;
        this.threshold = threshold;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getItemId() {
        return itemId;
    }

    public Date getTime() {
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
                ", id='" + itemId + '\'' +
                ", monitorStatus=" + monitorStatus +
                ", threshold=" + threshold +
                ", value=" + value +
                '}';
    }
}
