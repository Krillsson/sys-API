package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceSpeed {
    private final long rxbps;
    private final long txbps;

    public NetworkInterfaceSpeed(long rxbps, long txbps) {
        this.rxbps = rxbps;
        this.txbps = txbps;
    }

    public long getRxbps() {
        return rxbps;
    }

    public long getTxbps() {
        return txbps;
    }
}
