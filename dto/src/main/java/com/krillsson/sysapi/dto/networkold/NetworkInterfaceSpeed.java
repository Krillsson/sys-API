package com.krillsson.sysapi.dto.networkold;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rxbps",
        "txbps"
})
public class NetworkInterfaceSpeed {
    private long rxbps;
    private long txbps;

    /**
     * No args constructor for use in serialization
     */
    public NetworkInterfaceSpeed() {
    }

    /**
     * @param rxbps
     * @param txbps
     */
    public NetworkInterfaceSpeed(long rxbps, long txbps) {
        this.rxbps = rxbps;
        this.txbps = txbps;
    }

    @JsonProperty("rxbps")
    public long getRxbps() {
        return rxbps;
    }

    @JsonProperty("rxbps")
    public void setRxbps(long rxbps) {
        this.rxbps = rxbps;
    }

    @JsonProperty("txbps")
    public long getTxbps() {
        return txbps;
    }

    @JsonProperty("txbps")
    public void setTxbps(long txbps) {
        this.txbps = txbps;
    }

}
