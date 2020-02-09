package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import com.krillsson.sysapi.core.monitoring.MonitorType;

import java.time.Duration;

public class CpuTemperatureMonitor extends Monitor {
    public CpuTemperatureMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    public double value(SystemLoad systemLoad) {
        return systemLoad.getCpuLoad().getCpuHealth().getTemperatures().stream().findFirst().orElse(-1.0);
    }

    @Override
    public boolean isOutsideThreshold(double value) {
        return value > threshold();
    }

    @Override
    public MonitorType type() {
        return MonitorType.CPU_TEMP;
    }
}
