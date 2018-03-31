package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceLoad {
    private final String name;
    private final NetworkInterfaceValues metrics;
    private final NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(String name, NetworkInterfaceValues metrics, NetworkInterfaceSpeed speed) {
        this.name = name;
        this.metrics = metrics;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public NetworkInterfaceValues getMetrics() {
        return metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }
}
