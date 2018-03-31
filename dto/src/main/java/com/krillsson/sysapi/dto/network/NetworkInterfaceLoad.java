package com.krillsson.sysapi.dto.network;

public class NetworkInterfaceLoad {
    private String name;
    private NetworkInterfaceValues metrics;
    private NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(String name, NetworkInterfaceValues metrics, NetworkInterfaceSpeed speed) {
        this.metrics = metrics;
        this.speed = speed;
    }

    public NetworkInterfaceLoad() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkInterfaceValues getMetrics() {
        return metrics;
    }

    public void setMetrics(NetworkInterfaceValues metrics) {
        this.metrics = metrics;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(NetworkInterfaceSpeed speed) {
        this.speed = speed;
    }


}
