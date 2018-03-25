package com.krillsson.sysapi.core.domain.network;

public class NetworkInterface
{
    private final String name;
    private final String displayName;
    private final String mac;
    private final String[] ipv4;
    private final String[] ipv6;
    private final int mtu;
    private final boolean loopback;


    public NetworkInterface(String name, String displayName, String mac, int mtu, boolean loopback, String[] ipv4, String[] ipv6) {
        this.name = name;
        this.displayName = displayName;
        this.mac = mac;
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

    public String[] getIpv4() {
        return ipv4;
    }

    public String[] getIpv6() {
        return ipv6;
    }

    public int getMtu() {
        return mtu;
    }

    public boolean isLoopback() {
        return loopback;
    }
}
