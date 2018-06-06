package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;

import java.time.Duration;

import static com.krillsson.sysapi.core.metrics.Empty.DRIVE_LOAD;


public class DriveMonitor extends Monitor {
    public DriveMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    protected double value(SystemLoad systemLoad) {
        return (double) systemLoad.getDriveLoads()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(id()))
                .findFirst().orElse(DRIVE_LOAD)
                .getMetrics()
                .getUsableSpace();
    }

    @Override
    protected boolean isOutsideThreshold(double value) {
        return value < threshold();
    }

    @Override
    protected MonitorType type() {
        return MonitorType.DRIVE;
    }

    @Override
    public String toString() {
        return "DriveMonitor{" +
                "threshold=" + threshold() +
                '}';
    }
}
