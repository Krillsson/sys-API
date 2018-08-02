package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceLoad {
    private final String name;
    private final boolean up;
    private final NetworkInterfaceValues metrics;
    private final NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(String name, boolean up, NetworkInterfaceValues metrics, NetworkInterfaceSpeed speed) {
        this.name = name;
        this.up = up;
        this.metrics = metrics;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public boolean isUp() {
        return up;
    }

    public NetworkInterfaceValues getMetrics() {
        return metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }
}
