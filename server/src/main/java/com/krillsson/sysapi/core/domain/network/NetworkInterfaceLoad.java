package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceLoad {
    private final NetworkInterfaceValues metrics;
    private final NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(NetworkInterfaceValues metrics, NetworkInterfaceSpeed speed) {
        this.metrics = metrics;
        this.speed = speed;
    }

    public NetworkInterfaceValues getMetrics() {
        return metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }
}
