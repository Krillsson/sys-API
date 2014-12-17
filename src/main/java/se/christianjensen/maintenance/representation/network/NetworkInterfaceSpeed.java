package se.christianjensen.maintenance.representation.network;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NetworkInterfaceSpeed {
    private final long rxbps, txbps;

    public NetworkInterfaceSpeed(long rxbps, long txbps) {
        this.rxbps = rxbps;
        this.txbps = txbps;
    }

    @JsonProperty
    public long getRxbps() {
        return rxbps;
    }

    @JsonProperty
    public long getTxbps() {
        return txbps;
    }
}
