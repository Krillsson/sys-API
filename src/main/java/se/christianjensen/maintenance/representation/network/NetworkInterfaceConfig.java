package se.christianjensen.maintenance.representation.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.NetInterfaceConfig;

public final class NetworkInterfaceConfig {
    private final String name, hwaddr,
            type, description,
            address, destination,
            broadcast, netmask;
    private final long flags, mtu, metric;
    private NetworkInterfaceStatistics networkInterfaceStatistics;
    private NetworkInterfaceSpeed networkInterfaceSpeed;


    public NetworkInterfaceConfig(String name, String hwaddr,
                                  String type, String description,
                                  String address, String destination,
                                  String broadcast, String netmask,
                                  long flags, long mtu, long metric
    ) {
        this.name = name;
        this.hwaddr = hwaddr;
        this.type = type;
        this.description = description;
        this.address = address;
        this.destination = destination;
        this.broadcast = broadcast;
        this.netmask = netmask;
        this.flags = flags;
        this.mtu = mtu;
        this.metric = metric;
    }

    public static NetworkInterfaceConfig fromSigarBean(NetInterfaceConfig nIC) {
        return new NetworkInterfaceConfig(nIC.getName(), nIC.getHwaddr(),
                nIC.getType(), nIC.getDescription(),
                nIC.getAddress(), nIC.getDestination(),
                nIC.getBroadcast(), nIC.getNetmask(),
                nIC.getFlags(), nIC.getMtu(), nIC.getMetric());
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getHwaddr() {
        return hwaddr;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public String getAddress() {
        return address;
    }

    @JsonProperty
    public String getDestination() {
        return destination;
    }

    @JsonProperty
    public String getBroadcast() {
        return broadcast;
    }

    @JsonProperty
    public String getNetmask() {
        return netmask;
    }

    @JsonProperty
    public long getFlags() {
        return flags;
    }

    @JsonProperty
    public long getMtu() {
        return mtu;
    }

    @JsonProperty
    public long getMetric() {
        return metric;
    }

    @JsonProperty
    public NetworkInterfaceStatistics getNetworkInterfaceStatistics() {
        return networkInterfaceStatistics;
    }

    @JsonProperty
    public NetworkInterfaceSpeed getNetworkInterfaceSpeed() {
        return networkInterfaceSpeed;
    }

    public void setNetworkInterfaceStatistics(NetworkInterfaceStatistics networkInterfaceStatistics) {
        this.networkInterfaceStatistics = networkInterfaceStatistics;
    }

    public void setNetworkInterfaceSpeed(NetworkInterfaceSpeed networkInterfaceSpeed) {
        this.networkInterfaceSpeed = networkInterfaceSpeed;
    }
}
