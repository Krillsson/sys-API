package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceLoad {
    private final NetworkInterfaceMetrics metrics;
    private final NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(NetworkInterfaceMetrics metrics, NetworkInterfaceSpeed speed) {
        this.metrics = metrics;
        this.speed = speed;
    }

    public NetworkInterfaceMetrics getMetrics() {
        return metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }
}
