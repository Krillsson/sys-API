package com.krillsson.sysapi.dto.networkold;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "index",
        "parent",
        "virtual",
        "hardwareAddress",
        "loopback",
        "pointToPoint",
        "up"
})
public class NetworkInterface {

    @JsonProperty("index")
    private int index;
    @JsonProperty("parent")
    private NetworkInterface parent;
    @JsonProperty("virtual")
    private boolean virtual;
    @JsonProperty("loopback")
    private boolean loopback;
    @JsonProperty("pointToPoint")
    private boolean pointToPoint;
    @JsonProperty("up")
    private boolean up;

    /**
     * No args constructor for use in serialization
     */
    public NetworkInterface() {
    }

    /**
     * @param virtual
     * @param index
     * @param loopback
     * @param pointToPoint
     * @param parent
     * @param up
     */
    public NetworkInterface(int index, NetworkInterface parent, boolean virtual, boolean loopback, boolean pointToPoint, boolean up) {
        super();
        this.index = index;
        this.parent = parent;
        this.virtual = virtual;
        this.loopback = loopback;
        this.pointToPoint = pointToPoint;
        this.up = up;
    }

    @JsonProperty("index")
    public int getIndex() {
        return index;
    }

    @JsonProperty("index")
    public void setIndex(int index) {
        this.index = index;
    }

    @JsonProperty("parent")
    public NetworkInterface getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(NetworkInterface parent) {
        this.parent = parent;
    }

    @JsonProperty("virtual")
    public boolean getVirtual() {
        return virtual;
    }

    @JsonProperty("virtual")
    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    @JsonProperty("loopback")
    public boolean getLoopback() {
        return loopback;
    }

    @JsonProperty("loopback")
    public void setLoopback(boolean loopback) {
        this.loopback = loopback;
    }

    @JsonProperty("pointToPoint")
    public boolean getPointToPoint() {
        return pointToPoint;
    }

    @JsonProperty("pointToPoint")
    public void setPointToPoint(boolean pointToPoint) {
        this.pointToPoint = pointToPoint;
    }

    @JsonProperty("up")
    public boolean getUp() {
        return up;
    }

    @JsonProperty("up")
    public void setUp(boolean up) {
        this.up = up;
    }

}
