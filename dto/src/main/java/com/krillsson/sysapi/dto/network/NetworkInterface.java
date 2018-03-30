package com.krillsson.sysapi.dto.network;

import java.util.List;

public class NetworkInterface
{
    private String name;
    private String displayName;
    private String mac;
    private List<String> ipv4;
    private List<String> ipv6;
    private int mtu;
    private boolean loopback;


    public NetworkInterface(String name, String displayName, String mac, int mtu, boolean loopback, List<String> ipv4, List<String> ipv6) {
        this.name = name;
        this.displayName = displayName;
        this.mac = mac;
        this.mtu = mtu;
        this.loopback = loopback;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
    }

    public NetworkInterface() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIpv4(List<String> ipv4) {
        this.ipv4 = ipv4;
    }

    public void setIpv6(List<String> ipv6) {
        this.ipv6 = ipv6;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    public void setLoopback(boolean loopback) {
        this.loopback = loopback;
    }
}
