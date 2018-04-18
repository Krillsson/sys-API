package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;

public class DriveMonitor extends Monitor<Long> {
    private final Long threshold;
    private final Long near;

    public DriveMonitor(String id, Duration inertia, Long threshold, Long near) {
        super(id, inertia);
        this.threshold = threshold;
        this.near = near;
    }

    @Override
    protected Long value(SystemLoad systemLoad) {
        return systemLoad.getDriveLoads()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(getId()))
                .findFirst()
                .orElse(null)
                .getMetrics()
                .getUsableSpace();
    }

    @Override
    protected Long threshold() {
        return threshold;
    }


    @Override
    protected boolean isAboveThreshold(Long value) {
        return value < threshold();
    }

    @Override
    public String toString() {
        return "DriveMonitor{" +
                "threshold=" + threshold +
                ", near=" + near +
                '}';
    }
}
