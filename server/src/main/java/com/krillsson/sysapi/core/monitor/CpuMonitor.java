package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;

public class CpuMonitor extends Monitor<Double> {
    private final double threshold;
    private final double near;

    public CpuMonitor(String id, Duration inertia, double threshold, double near) {
        super(id, inertia);
        this.threshold = threshold;
        this.near = near;
    }

    @Override
    protected Double value(SystemLoad systemLoad) {
        return systemLoad.getCpuLoad().getCpuLoadOsMxBean();
    }

    @Override
    protected Double threshold() {
        return threshold;
    }

    @Override
    protected boolean isAboveThreshold(Double value) {
        return value > threshold();
    }

    @Override
    public String toString() {
        return "CpuMonitor{" +
                "threshold=" + threshold +
                ", near=" + near +
                '}';
    }
}
