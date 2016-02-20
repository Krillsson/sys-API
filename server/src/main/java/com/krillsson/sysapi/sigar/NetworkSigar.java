package com.krillsson.sysapi.sigar;


import org.hyperic.sigar.*;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.network.NetworkInterfaceSpeed;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NetworkSigar extends SigarWrapper {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(NetworkSigar.class.getSimpleName());

    final int SPEED_MEASUREMENT_PERIOD = 100;
    final int BYTE_TO_BIT = 8;
    final static String NOT_FOUND_STRING = "No %s with id '%s' were found";

    protected NetworkSigar(Sigar sigar) {
        super(sigar);
    }

    public List<NetworkInterfaceConfig> getConfigs() {
        String[] netIfs;
        ArrayList<NetworkInterfaceConfig> configs = new ArrayList<>();
        try {
            netIfs = sigar.getNetInterfaceList();
            for (String name : netIfs) {
                NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(name);
                if(((netInterfaceConfig.getFlags() & 1 << NetFlags.IFF_UP) != 0)  && ((netInterfaceConfig.getFlags() & 1 << NetFlags.IFF_RUNNING) != 0)){
                    NetworkInterfaceConfig networkInterfaceConfig = SigarBeanConverter.fromSigarBean(netInterfaceConfig);
                    networkInterfaceConfig.setNetworkInterfaceStatistics(SigarBeanConverter.fromSigarBean(sigar.getNetInterfaceStat(name)));
                    configs.add(networkInterfaceConfig);
                }
            }
        } catch (SigarException e) {
            throw new IllegalArgumentException(e);
        }
        if (!configs.isEmpty()) {
            return configs;
        } else {
            throw new IllegalArgumentException("No network interfaces where found");
        }
    }

    public NetworkInterfaceConfig getConfigById(String id) {
        NetworkInterfaceConfig config;
        try {
            NetInterfaceConfig sigarConfig = sigar.getNetInterfaceConfig(id);
            if(((sigarConfig.getFlags() & 1 << NetFlags.IFF_UP) != 0)  && ((sigarConfig.getFlags() & 1 << NetFlags.IFF_RUNNING) != 0)) {
                config = SigarBeanConverter.fromSigarBean(sigarConfig);
                config.setNetworkInterfaceStatistics(SigarBeanConverter.fromSigarBean(sigar.getNetInterfaceStat(id)));
            }
            else{
                throw new IllegalArgumentException(String.format(NOT_FOUND_STRING, NetworkInterfaceConfig.class.getSimpleName(), id));
            }
        } catch (SigarException | IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(NOT_FOUND_STRING, NetworkInterfaceConfig.class.getSimpleName(), id), e);
        }
        return config;
    }

    public NetworkInfo getNetworkInfo() {
        NetInfo sigarNetInfo;
        NetworkInfo networkInfo = null;
        List<NetworkInterfaceConfig> configs;

        try {
            sigarNetInfo = sigar.getNetInfo();
            configs = getConfigs();
            networkInfo = SigarBeanConverter.fromSigarBean(sigarNetInfo, configs);
        } catch (SigarException e) {
            throw new IllegalArgumentException(e.getCause());
        }
        return networkInfo;
    }

    public NetworkInterfaceSpeed getSpeed(String networkInterfaceConfigName) {
        long rxbps, txbps;
        long start = 0;
        long end = 0;
        long rxBytesStart = 0;
        long rxBytesEnd = 0;
        long txBytesStart = 0;
        long txBytesEnd = 0;

        try {
            start = System.currentTimeMillis();
            NetInterfaceStat statStart = sigar.getNetInterfaceStat(networkInterfaceConfigName);
            rxBytesStart = statStart.getRxBytes();
            txBytesStart = statStart.getTxBytes();
            Thread.sleep(SPEED_MEASUREMENT_PERIOD);
            NetInterfaceStat statEnd = sigar.getNetInterfaceStat(networkInterfaceConfigName);
            end = System.currentTimeMillis();
            rxBytesEnd = statEnd.getRxBytes();
            txBytesEnd = statEnd.getTxBytes();
        } catch (SigarException e) {
            throw new IllegalArgumentException(String.format(NOT_FOUND_STRING, NetworkInterfaceConfig.class.getSimpleName(), networkInterfaceConfigName));
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Interrupted while measuring networkspeed", e);
        }

        rxbps = measureSpeed(start, end, rxBytesStart, rxBytesEnd);
        txbps = measureSpeed(start, end, txBytesStart, txBytesEnd);
        return new NetworkInterfaceSpeed(rxbps, txbps);
    }

    private long measureSpeed(long start, long end, long rxBytesStart, long rxBytesEnd) {
        return (rxBytesEnd - rxBytesStart) * BYTE_TO_BIT / (end - start) * SPEED_MEASUREMENT_PERIOD;
    }
}
