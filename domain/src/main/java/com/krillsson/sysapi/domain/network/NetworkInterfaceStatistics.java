package com.krillsson.sysapi.domain.network;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NetworkInterfaceStatistics {
    private final long rxBytes, txBytes;
    private final long rxPackets, txPackets;
    private final long rxErrors, txErrors;
    private final long rxDropped, txDropped;
    private final long rxOverruns, txOverruns;
    private final long rxFrame, txCollisions, txCarrier, speed; //speed == bit/s

    public NetworkInterfaceStatistics(long rxBytes, long rxPackets,
                                      long rxErrors, long rxDropped,
                                      long rxOverruns, long rxFrame,
                                      long txBytes, long txPackets,
                                      long txErrors, long txDropped,
                                      long txOverruns, long txCollisions,
                                      long txCarrier, long speed) {
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.rxErrors = rxErrors;
        this.rxDropped = rxDropped;
        this.rxOverruns = rxOverruns;
        this.rxFrame = rxFrame;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.txErrors = txErrors;
        this.txDropped = txDropped;
        this.txOverruns = txOverruns;
        this.txCollisions = txCollisions;
        this.txCarrier = txCarrier;
        this.speed = speed;
    }

    @JsonProperty
    public long getRxBytes() {
        return rxBytes;
    }

    @JsonProperty
    public long getRxPackets() {
        return rxPackets;
    }

    @JsonProperty
    public long getRxErrors() {
        return rxErrors;
    }

    @JsonProperty
    public long getRxDropped() {
        return rxDropped;
    }

    @JsonProperty
    public long getRxOverruns() {
        return rxOverruns;
    }

    @JsonProperty
    public long getRxFrame() {
        return rxFrame;
    }

    @JsonProperty
    public long getTxBytes() {
        return txBytes;
    }

    @JsonProperty
    public long getTxPackets() {
        return txPackets;
    }

    @JsonProperty
    public long getTxErrors() {
        return txErrors;
    }

    @JsonProperty
    public long getTxDropped() {
        return txDropped;
    }

    @JsonProperty
    public long getTxOverruns() {
        return txOverruns;
    }

    @JsonProperty
    public long getTxCollisions() {
        return txCollisions;
    }

    @JsonProperty
    public long getTxCarrier() {
        return txCarrier;
    }

    @JsonProperty
    public long getSpeed() {
        return speed;
    }
}
