package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;

public class CpuMonitor extends Monitor {
    private final double threshold;

    public CpuMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia);
        this.threshold = threshold;
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return systemLoad.getCpuLoad().getCpuLoadOsMxBean();
    }

    @Override
    protected double threshold() {
        return threshold;
    }

    @Override
    protected boolean isAboveThreshold(double value) {
        return value > threshold();
    }

    @Override
    public String toString() {
        return "CpuMonitor{" +
                "threshold=" + threshold +
                '}';
    }
}
