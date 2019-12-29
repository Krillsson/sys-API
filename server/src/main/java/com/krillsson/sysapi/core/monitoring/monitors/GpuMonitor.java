package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import com.krillsson.sysapi.core.monitoring.MonitorType;

import java.time.Duration;

public class GpuMonitor extends Monitor {
    public GpuMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return systemLoad.getGpuLoads().stream().filter(n -> n.getName().equalsIgnoreCase(id())).mapToDouble(GpuLoad::getCoreLoad).findFirst().orElse(-1.0);
    }

    @Override
    protected boolean isOutsideThreshold(double value) {
        return value > threshold();
    }

    @Override
    protected MonitorType type() {
        return MonitorType.GPU_LOAD;
    }
}
