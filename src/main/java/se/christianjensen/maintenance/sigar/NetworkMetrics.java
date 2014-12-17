package se.christianjensen.maintenance.sigar;


import org.hyperic.sigar.*;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceConfig;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceSpeed;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceStatistics;

import java.util.ArrayList;
import java.util.List;

public class NetworkMetrics extends AbstractSigarMetric {


    protected NetworkMetrics(Sigar sigar) {
        super(sigar);
    }

    public List<NetworkInterfaceConfig> getConfigs() {
        String[] netIfs;
        ArrayList<NetworkInterfaceConfig> configs = new ArrayList<>();
        try {
            netIfs = sigar.getNetInterfaceList();
            for (String name : netIfs) {
                NetworkInterfaceConfig networkInterfaceConfig = NetworkInterfaceConfig.fromSigarBean(sigar.getNetInterfaceConfig(name));
                networkInterfaceConfig.setNetworkInterfaceStatistics(NetworkInterfaceStatistics.fromSigarBean(sigar.getNetInterfaceStat(name)));
                networkInterfaceConfig.setNetworkInterfaceSpeed(getSpeed(name));
                configs.add(networkInterfaceConfig);
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

    public NetworkInfo getNetworkInfo() {
        NetInfo sigarNetInfo = null;
        NetworkInfo networkInfo = null;
        List<NetworkInterfaceConfig> configs;

        try {
            sigarNetInfo = sigar.getNetInfo();
            configs = getConfigs();
            networkInfo = NetworkInfo.fromSigarBean(sigarNetInfo, configs);
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return networkInfo;
    }

    public NetworkInterfaceConfig getConfigById(String id) throws SigarException {
        NetworkInterfaceConfig config;

        try {
            NetInterfaceConfig sigarConfig = sigar.getNetInterfaceConfig(id);
            config = NetworkInterfaceConfig.fromSigarBean(sigarConfig);
            config.setNetworkInterfaceStatistics(NetworkInterfaceStatistics.fromSigarBean(sigar.getNetInterfaceStat(id)));
            config.setNetworkInterfaceSpeed(getSpeed(id));
        } catch (SigarException e) {
            throw new IllegalArgumentException("No networkinterfaceconfig with id " + id + "where found");
        }

        return config;
    }

    public NetworkInterfaceSpeed getSpeed(String networkInterfaceConfigName) {
        long rxbps, txbps;
        long start = 0;
        long end = 0;
        long rxBytesStart = 0;
        long rxBytesEnd = 0;
        long txBytesStart = 0;
        long txBytesEnd = 0;

        start = System.currentTimeMillis();
        try {
            NetInterfaceStat statStart = sigar.getNetInterfaceStat(networkInterfaceConfigName);
            rxBytesStart = statStart.getRxBytes();
            txBytesStart = statStart.getTxBytes();
            Thread.sleep(100);
            NetInterfaceStat statEnd = sigar.getNetInterfaceStat(networkInterfaceConfigName);
            end = System.currentTimeMillis();
            rxBytesEnd = statEnd.getRxBytes();
            txBytesEnd = statEnd.getTxBytes();
        } catch (InterruptedException | SigarException ie) {
            //give up
        }

        rxbps = (rxBytesEnd - rxBytesStart) * 8 / (end - start) * 100;
        txbps = (txBytesEnd - txBytesStart) * 8 / (end - start) * 100;
        return new NetworkInterfaceSpeed(rxbps, txbps);
    }


}
