
package com.krillsson.sysapi.dto.network;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "networkInterface",
    "mtu",
    "bytesRecv",
    "bytesSent",
    "packetsRecv",
    "packetsSent",
    "inErrors",
    "outErrors",
    "speed",
    "timeStamp",
    "name",
    "displayName",
    "ipv6addr",
    "ipv4addr",
    "macaddr"
})
public class NetworkIF {

    @JsonProperty("networkInterface")
    private NetworkInterface networkInterface;
    @JsonProperty("mtu")
    private Integer mtu;
    @JsonProperty("bytesRecv")
    private Integer bytesRecv;
    @JsonProperty("bytesSent")
    private Integer bytesSent;
    @JsonProperty("packetsRecv")
    private Integer packetsRecv;
    @JsonProperty("packetsSent")
    private Integer packetsSent;
    @JsonProperty("inErrors")
    private Integer inErrors;
    @JsonProperty("outErrors")
    private Integer outErrors;
    @JsonProperty("speed")
    private Integer speed;
    @JsonProperty("timeStamp")
    private Integer timeStamp;
    @JsonProperty("name")
    private String name;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("ipv6addr")
    private String[] ipv6addr = null;
    @JsonProperty("ipv4addr")
    private String[] ipv4addr = null;
    @JsonProperty("macaddr")
    private String macaddr;

    /**
     * No args constructor for use in serialization
     * 
     */
    public NetworkIF() {
    }

    /**
     * 
     * @param mtu
     * @param bytesSent
     * @param packetsSent
     * @param timeStamp
     * @param speed
     * @param ipv4addr
     * @param ipv6addr
     * @param inErrors
     * @param macaddr
     * @param networkInterface
     * @param bytesRecv
     * @param packetsRecv
     * @param name
     * @param outErrors
     * @param displayName
     */
    public NetworkIF(NetworkInterface networkInterface, Integer mtu, Integer bytesRecv, Integer bytesSent, Integer packetsRecv, Integer packetsSent, Integer inErrors, Integer outErrors, Integer speed, Integer timeStamp, String name, String displayName, String[] ipv6addr, String[] ipv4addr, String macaddr) {
        super();
        this.networkInterface = networkInterface;
        this.mtu = mtu;
        this.bytesRecv = bytesRecv;
        this.bytesSent = bytesSent;
        this.packetsRecv = packetsRecv;
        this.packetsSent = packetsSent;
        this.inErrors = inErrors;
        this.outErrors = outErrors;
        this.speed = speed;
        this.timeStamp = timeStamp;
        this.name = name;
        this.displayName = displayName;
        this.ipv6addr = ipv6addr;
        this.ipv4addr = ipv4addr;
        this.macaddr = macaddr;
    }

    @JsonProperty("networkInterface")
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    @JsonProperty("networkInterface")
    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    @JsonProperty("mtu")
    public Integer getMtu() {
        return mtu;
    }

    @JsonProperty("mtu")
    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    @JsonProperty("bytesRecv")
    public Integer getBytesRecv() {
        return bytesRecv;
    }

    @JsonProperty("bytesRecv")
    public void setBytesRecv(Integer bytesRecv) {
        this.bytesRecv = bytesRecv;
    }

    @JsonProperty("bytesSent")
    public Integer getBytesSent() {
        return bytesSent;
    }

    @JsonProperty("bytesSent")
    public void setBytesSent(Integer bytesSent) {
        this.bytesSent = bytesSent;
    }

    @JsonProperty("packetsRecv")
    public Integer getPacketsRecv() {
        return packetsRecv;
    }

    @JsonProperty("packetsRecv")
    public void setPacketsRecv(Integer packetsRecv) {
        this.packetsRecv = packetsRecv;
    }

    @JsonProperty("packetsSent")
    public Integer getPacketsSent() {
        return packetsSent;
    }

    @JsonProperty("packetsSent")
    public void setPacketsSent(Integer packetsSent) {
        this.packetsSent = packetsSent;
    }

    @JsonProperty("inErrors")
    public Integer getInErrors() {
        return inErrors;
    }

    @JsonProperty("inErrors")
    public void setInErrors(Integer inErrors) {
        this.inErrors = inErrors;
    }

    @JsonProperty("outErrors")
    public Integer getOutErrors() {
        return outErrors;
    }

    @JsonProperty("outErrors")
    public void setOutErrors(Integer outErrors) {
        this.outErrors = outErrors;
    }

    @JsonProperty("speed")
    public Integer getSpeed() {
        return speed;
    }

    @JsonProperty("speed")
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @JsonProperty("timeStamp")
    public Integer getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("timeStamp")
    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("ipv6addr")
    public String[] getIpv6addr() {
        return ipv6addr;
    }

    @JsonProperty("ipv6addr")
    public void setIpv6addr(String[] ipv6addr) {
        this.ipv6addr = ipv6addr;
    }

    @JsonProperty("ipv4addr")
    public String[] getIpv4addr() {
        return ipv4addr;
    }

    @JsonProperty("ipv4addr")
    public void setIpv4addr(String[] ipv4addr) {
        this.ipv4addr = ipv4addr;
    }

    @JsonProperty("macaddr")
    public String getMacaddr() {
        return macaddr;
    }

    @JsonProperty("macaddr")
    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

}
