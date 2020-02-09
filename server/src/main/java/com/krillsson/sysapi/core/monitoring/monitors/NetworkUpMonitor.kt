package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import com.krillsson.sysapi.core.monitoring.MonitorType;

import java.time.Duration;

public class NetworkUpMonitor extends Monitor {
    public NetworkUpMonitor(String id, Duration inertia, double threshold) {
        super(id, inertia, threshold);
    }

    @Override
    public double value(SystemLoad systemLoad) {
        return systemLoad.getNetworkInterfaceLoads()
                .stream()
                .filter(n -> n.getName().equalsIgnoreCase(id()))
                .map(n -> n.isUp() ? 1.0 : 0.0)
                .findFirst()
                .orElse(0.0);
    }

    @Override
    public boolean isOutsideThreshold(double value) {
        return value != 0.0;
    }

    @Override
    public MonitorType type() {
        return MonitorType.NETWORK_UP;
    }
}
