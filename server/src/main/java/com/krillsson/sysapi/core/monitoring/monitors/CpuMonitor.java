package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import com.krillsson.sysapi.core.monitoring.MonitorType;

import java.time.Duration;

public class CpuMonitor extends Monitor {

    public CpuMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return systemLoad.getCpuLoad().getCpuLoadOsMxBean();
    }

    @Override
    protected boolean isOutsideThreshold(double value) {
        return value > threshold();
    }

    @Override
    protected MonitorType type() {
        return MonitorType.CPU_LOAD;
    }

    @Override
    public String toString() {
        return "CpuMonitor{" +
                "threshold=" + threshold() +
                '}';
    }
}
