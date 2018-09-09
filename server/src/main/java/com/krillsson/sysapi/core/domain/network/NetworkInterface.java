package com.krillsson.sysapi.core.domain.network;

import java.util.List;

public class NetworkInterface {
    private final String name;
    private final String displayName;
    private final String mac;
    private final long speed;
    private final List<String> ipv4;
    private final List<String> ipv6;
    private final int mtu;
    private final boolean loopback;


    public NetworkInterface(String name, String displayName, String mac, long speed, int mtu, boolean loopback, List<String> ipv4, List<String> ipv6) {
        this.name = name;
        this.displayName = displayName;
        this.mac = mac;
        this.speed = speed;
        this.mtu = mtu;
        this.loopback = loopback;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMac() {
        return mac;
    }

    public long getSpeed() {
        return speed;
    }

    public List<String> getIpv4() {
        return ipv4;
    }

    public List<String> getIpv6() {
        return ipv6;
    }

    public int getMtu() {
        return mtu;
    }

    public boolean isLoopback() {
        return loopback;
    }
}
