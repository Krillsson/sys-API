package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceLoad {
    private final String name;
    private final boolean up;
    private final NetworkInterfaceValues values;
    private final NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(String name, boolean up, NetworkInterfaceValues values, NetworkInterfaceSpeed speed) {
        this.name = name;
        this.up = up;
        this.values = values;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public boolean isUp() {
        return up;
    }

    public NetworkInterfaceValues getValues() {
        return values;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }
}
