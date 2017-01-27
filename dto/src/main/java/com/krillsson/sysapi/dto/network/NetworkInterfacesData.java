package com.krillsson.sysapi.dto.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "networkIFs",
        "timeStamp"
})
public class NetworkInterfacesData {

    @JsonProperty("networkIFs")
    private NetworkIF[] networkIFs = null;
    @JsonProperty("timeStamp")
    private long timeStamp;

    /**
     * No args constructor for use in serialization
     */
    public NetworkInterfacesData() {
    }

    /**
     * @param networkIFs
     * @param timeStamp
     */
    public NetworkInterfacesData(NetworkIF[] networkIFs, Integer timeStamp) {
        super();
        this.networkIFs = networkIFs;
        this.timeStamp = timeStamp;
    }

    @JsonProperty("networkIFs")
    public NetworkIF[] getNetworkIFs() {
        return networkIFs;
    }

    @JsonProperty("networkIFs")
    public void setNetworkIFs(NetworkIF[] networkIFs) {
        this.networkIFs = networkIFs;
    }

    @JsonProperty("timeStamp")
    public long getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("timeStamp")
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
