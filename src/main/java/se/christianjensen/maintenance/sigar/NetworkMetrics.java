package se.christianjensen.maintenance.sigar;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.*;

import java.util.ArrayList;
import java.util.List;

public class NetworkMetrics extends AbstractSigarMetric {


    protected NetworkMetrics(Sigar sigar) {
        super(sigar);
    }

    public static final class NetworkInterfaceConfig {
        private final String name, hwaddr,
                type, description,
                address, destination,
                broadcast, netmask;
        private final long flags, mtu, metric;


        public NetworkInterfaceConfig(String name, String hwaddr,
                                      String type, String description,
                                      String address, String destination,
                                      String broadcast, String netmask,
                                      long flags, long mtu, long metric) {
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
    }

    public static final class NetworkInfo {
        private final String defaultGateway, hostName, domainName, primaryDns, secondaryDns;

        private List<NetworkInterfaceConfig> networkInterfaceConfigs;

        public NetworkInfo(String defaultGateway, String hostName, String domainName, String primaryDns, String secondaryDns) {
            this.defaultGateway = defaultGateway;
            this.hostName = hostName;
            this.domainName = domainName;
            this.primaryDns = primaryDns;
            this.secondaryDns = secondaryDns;
        }

        public NetworkInfo(String defaultGateway, String hostName, String domainName, String primaryDns, String secondaryDns,
                List<NetworkInterfaceConfig> networkInterfaceConfigs) {
            this(defaultGateway,hostName,domainName,primaryDns,secondaryDns);
            this.networkInterfaceConfigs = networkInterfaceConfigs;
        }

        public static NetworkInfo fromSigarBean(NetInfo ni, List<NetworkInterfaceConfig> configs) {
            return new NetworkInfo(ni.getDefaultGateway()
                    , ni.getHostName(), ni.getDomainName()
                    , ni.getPrimaryDns(), ni.getSecondaryDns()
                    ,configs);
        }

        @JsonProperty
        public String getDefaultGateway() {
            return defaultGateway;
        }

        @JsonProperty
        public String getHostName() {
            return hostName;
        }

        @JsonProperty
        public String getDomainName() {
            return domainName;
        }

        @JsonProperty
        public String getPrimaryDns() {
            return primaryDns;
        }

        @JsonProperty
        public String getSecondaryDns() {
            return secondaryDns;
        }
        @JsonProperty
        public List<NetworkInterfaceConfig> getNetworkInterfaceConfigs() {
            return networkInterfaceConfigs;
        }
    }

    public static final class NetworkInterfaceStatistics {
        private final long rxBytes, rxPackets,
                rxErrors, rxDropped, rxOverruns,
                rxFrame, txBytes, txPackets,
                txErrors, txDropped, txOverruns,
                txCollisions, txCarrier, speed;

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

        public NetworkInterfaceStatistics fromSigarBean(NetInterfaceStat nIS) {

            return new NetworkInterfaceStatistics(nIS.getRxBytes(), nIS.getRxPackets(),
                    nIS.getRxErrors(), nIS.getRxDropped(),
                    nIS.getRxOverruns(), nIS.getRxFrame(),
                    nIS.getTxBytes(), nIS.getTxPackets(),
                    nIS.getTxErrors(), nIS.getTxDropped(),
                    nIS.getTxOverruns(), nIS.getTxCollisions(),
                    nIS.getTxCarrier(), nIS.getSpeed());
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

    public List<NetworkInterfaceConfig> getConfigs() {
        String[] netIfs = null;
        ArrayList<NetworkInterfaceConfig> configs = new ArrayList<>();
        try {
            netIfs = sigar.getNetInterfaceList();
            for (String name : netIfs) {
                configs.add(NetworkInterfaceConfig.fromSigarBean(sigar.getNetInterfaceConfig(name)));
            }
        } catch (SigarException e) {
            //derp
        }
        if (!configs.isEmpty()) {
            return configs;
        } else {
            throw new IllegalArgumentException("No network interfaces where found");
        }
    }

    @JsonProperty
    public NetworkInfo getNetworkInfo(){
        NetInfo sigarNetInfo = null;
        NetworkInfo networkInfo = null;
        List<NetworkInterfaceConfig> configs;

        try {
            sigarNetInfo = sigar.getNetInfo();
            configs = getConfigs();
            networkInfo = NetworkInfo.fromSigarBean(sigarNetInfo,configs);
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return networkInfo;
    }


}
