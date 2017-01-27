package com.krillsson.sysapi.dto.network;

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
    private Integer index;
    @JsonProperty("parent")
    private NetworkInterface parent;
    @JsonProperty("virtual")
    private Boolean virtual;
    @JsonProperty("loopback")
    private Boolean loopback;
    @JsonProperty("pointToPoint")
    private Boolean pointToPoint;
    @JsonProperty("up")
    private Boolean up;

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
    public NetworkInterface(Integer index, NetworkInterface parent, Boolean virtual, Boolean loopback, Boolean pointToPoint, Boolean up) {
        super();
        this.index = index;
        this.parent = parent;
        this.virtual = virtual;
        this.loopback = loopback;
        this.pointToPoint = pointToPoint;
        this.up = up;
    }

    @JsonProperty("index")
    public Integer getIndex() {
        return index;
    }

    @JsonProperty("index")
    public void setIndex(Integer index) {
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
    public Boolean getVirtual() {
        return virtual;
    }

    @JsonProperty("virtual")
    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    @JsonProperty("loopback")
    public Boolean getLoopback() {
        return loopback;
    }

    @JsonProperty("loopback")
    public void setLoopback(Boolean loopback) {
        this.loopback = loopback;
    }

    @JsonProperty("pointToPoint")
    public Boolean getPointToPoint() {
        return pointToPoint;
    }

    @JsonProperty("pointToPoint")
    public void setPointToPoint(Boolean pointToPoint) {
        this.pointToPoint = pointToPoint;
    }

    @JsonProperty("up")
    public Boolean getUp() {
        return up;
    }

    @JsonProperty("up")
    public void setUp(Boolean up) {
        this.up = up;
    }

}
