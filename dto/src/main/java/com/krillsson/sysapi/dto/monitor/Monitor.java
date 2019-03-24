package com.krillsson.sysapi.dto.monitor;

public class Monitor {

    private String id;
    private long inertiaInSeconds;
    private MonitorType type;
    private Double threshold;

    public Monitor(String id, long inertiaInSeconds, MonitorType type, Double threshold) {
        this.id = id;
        this.inertiaInSeconds = inertiaInSeconds;
        this.type = type;
        this.threshold = threshold;
    }

    public Monitor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getInertiaInSeconds() {
        return inertiaInSeconds;
    }

    public void setInertiaInSeconds(long inertiaInSeconds) {
        this.inertiaInSeconds = inertiaInSeconds;
    }

    public MonitorType getType() {
        return type;
    }

    public void setType(MonitorType type) {
        this.type = type;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "Monitor{" +
                "id='" + id + '\'' +
                ", inertiaInSeconds=" + inertiaInSeconds +
                ", type=" + type +
                ", threshold=" + threshold +
                '}';
    }
}
