package com.krillsson.sysapi.dto.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "networkIF",
        "networkInterfaceSpeed"
})
public class NetworkInterfaceData {

    @JsonProperty("networkIF")
    private NetworkIF networkIF;
    private NetworkInterfaceSpeed networkInterfaceSpeed;

    /**
     * No args constructor for use in serialization
     */
    public NetworkInterfaceData() {
    }

    /**
     * @param networkIF
     * @param networkInterfaceSpeed
     */
    public NetworkInterfaceData(NetworkIF networkIF, NetworkInterfaceSpeed networkInterfaceSpeed) {
        super();
        this.networkIF = networkIF;
        this.networkInterfaceSpeed = networkInterfaceSpeed;
    }

    @JsonProperty("networkIF")
    public NetworkIF getNetworkIF() {
        return networkIF;
    }

    @JsonProperty("networkIF")
    public void setNetworkIF(NetworkIF networkIF) {
        this.networkIF = networkIF;
    }

    @JsonProperty("networkInterfaceSpeed")
    public NetworkInterfaceSpeed getNetworkInterfaceSpeed() {
        return networkInterfaceSpeed;
    }

    @JsonProperty("networkInterfaceSpeed")
    public void setNetworkInterfaceSpeed(NetworkInterfaceSpeed networkInterfaceSpeed) {
        this.networkInterfaceSpeed = networkInterfaceSpeed;
    }

}
