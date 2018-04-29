package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;


public class DriveMonitor extends Monitor {
    private final double threshold;

    public DriveMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia);
        this.threshold = threshold;
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return (double) systemLoad.getDriveLoads()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(id()))
                .findFirst()
                .orElse(null)
                .getMetrics()
                .getUsableSpace();
    }

    @Override
    protected double threshold() {
        return threshold;
    }


    @Override
    protected boolean isAboveThreshold(double value) {
        return value < threshold();
    }

    @Override
    public String toString() {
        return "DriveMonitor{" +
                "threshold=" + threshold +
                '}';
    }
}
