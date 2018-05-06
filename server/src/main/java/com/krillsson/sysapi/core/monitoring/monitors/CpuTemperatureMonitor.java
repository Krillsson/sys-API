package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;

import java.time.Duration;

public class CpuTemperatureMonitor extends Monitor {
    public CpuTemperatureMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return systemLoad.getCpuLoad().getCpuHealth().getTemperatures().stream().findFirst().orElse(-1.0);
    }

    @Override
    protected boolean isAboveThreshold(double value) {
        return value > threshold();
    }

    @Override
    protected MonitorType type() {
        return MonitorType.CPU_TEMP;
    }
}
