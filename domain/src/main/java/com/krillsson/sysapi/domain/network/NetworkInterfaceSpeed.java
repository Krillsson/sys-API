package com.krillsson.sysapi.domain.network;

public final class NetworkInterfaceSpeed {
    private final long rxbps, txbps;

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
