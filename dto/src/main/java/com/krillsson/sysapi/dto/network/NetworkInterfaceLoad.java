package com.krillsson.sysapi.dto.network;

public class NetworkInterfaceLoad {
    private String name;
    private boolean up;
    private NetworkInterfaceValues values;
    private NetworkInterfaceSpeed speed;

    public NetworkInterfaceLoad(String name, boolean up, NetworkInterfaceValues values, NetworkInterfaceSpeed speed) {
        this.values = values;
        this.up = up;
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

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public NetworkInterfaceValues getValues() {
        return values;
    }

    public void setValues(NetworkInterfaceValues values) {
        this.values = values;
    }

    public NetworkInterfaceSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(NetworkInterfaceSpeed speed) {
        this.speed = speed;
    }


}
