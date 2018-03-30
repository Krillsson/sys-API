package com.krillsson.sysapi.dto.network;

public class NetworkInterfaceLoad {
    private NetworkInterfaceValues metrics;
    private NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(NetworkInterfaceValues metrics, NetworkInterfaceSpeed speed) {
        this.metrics = metrics;
        this.speed = speed;
    }

    public NetworkInterfaceLoad() {
    }

    public NetworkInterfaceValues getMetrics() {
        return metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }

    public void setMetrics(NetworkInterfaceValues metrics) {
        this.metrics = metrics;
    }

    public void setSpeed(NetworkInterfaceSpeed speed) {
        this.speed = speed;
    }
}
