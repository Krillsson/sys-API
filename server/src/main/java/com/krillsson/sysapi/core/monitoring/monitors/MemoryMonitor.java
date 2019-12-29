package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import com.krillsson.sysapi.core.monitoring.MonitorType;

import java.time.Duration;

public class MemoryMonitor extends Monitor {
    public MemoryMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return systemLoad.getMemory().getAvailable();
    }

    @Override
    protected boolean isOutsideThreshold(double value) {
        return value < threshold();
    }

    @Override
    protected MonitorType type() {
        return MonitorType.MEMORY_SPACE;
    }
}
