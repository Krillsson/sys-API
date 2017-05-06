package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.domain.network.SpeedMeasurement;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultNetworkProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultNetworkProvider.class);

    private static final int BYTE_TO_BIT = 8;

    private final HardwareAbstractionLayer hal;
    private final SpeedMeasurementManager speedMeasurementManager;

    DefaultNetworkProvider(HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        this.hal = hal;
        this.speedMeasurementManager = speedMeasurementManager;
        register();
    }

    private void register() {
        List<SpeedMeasurementManager.SpeedSource> collect = Arrays.stream(hal.getNetworkIFs()).map(n -> new SpeedMeasurementManager.SpeedSource() {
            @Override
            public String getName() {
                return n.getName();
            }

            @Override
            public long getCurrentRead() {
                n.updateNetworkStats();
                return n.getBytesRecv() * BYTE_TO_BIT;
            }

            @Override
            public long getCurrentWrite() {
                n.updateNetworkStats();
                return n.getBytesSent() * BYTE_TO_BIT;
            }
        }).collect(Collectors.toList());
        speedMeasurementManager.register(collect);
    }

    String[] getNetworkInterfaceNames(){
        return Arrays.stream(hal.getNetworkIFs()).map(NetworkIF::getName).toArray(String[]::new);
    }

    NetworkInterfaceData[] getAllNetworkInterfaces() {
        return Arrays.stream(hal.getNetworkIFs()).map(n -> new NetworkInterfaceData(n, networkInterfaceSpeed(n.getName()))).toArray(NetworkInterfaceData[]::new);
    }

    Optional<NetworkInterfaceData> getNetworkInterfaceById(String id) {
        return Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).map(nic -> new NetworkInterfaceData(nic, networkInterfaceSpeed(nic.getName()))).findFirst();
    }

    Optional<NetworkInterfaceSpeed> getSpeed(String id) {
        Optional<NetworkIF> networkOptional = Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).findAny();
        if (networkOptional.isPresent()) {
            NetworkIF networkIF = networkOptional.get();
            return Optional.of(networkInterfaceSpeed(networkIF.getName()));
        } else {
            return Optional.empty();
        }
    }

    private NetworkInterfaceSpeed networkInterfaceSpeed(String name){
        SpeedMeasurementManager.CurrentSpeed currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(name);
        return new NetworkInterfaceSpeed(currentSpeedForName.getReadPerSeconds(), currentSpeedForName.getWritePerSeconds());
    }

}
