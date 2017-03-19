package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeedMeasurement;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class DefaultNetworkProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultNetworkProvider.class);

    private HashMap<String, NetworkInterfaceSpeedMeasurement> nicSpeedStore = new HashMap<>();

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 1000;
    private static final int BYTE_TO_BIT = 8;

    private final HardwareAbstractionLayer hal;
    private final Utils utils;

    DefaultNetworkProvider(HardwareAbstractionLayer hal, Utils utils) {
        this.hal = hal;
        this.utils = utils;
    }

    NetworkInterfaceData[] getAllNetworkInterfaces() {
        return Arrays.stream(hal.getNetworkIFs()).map(m -> {
            m.updateNetworkStats();
            return new NetworkInterfaceData(m, getSpeed(m.getName()).get());
        }).toArray(NetworkInterfaceData[]::new);
    }

    Optional<NetworkInterfaceData> getNetworkInterfaceById(String id) {
        return Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).map(m -> {
            m.updateNetworkStats();
            return new NetworkInterfaceData(m, getSpeed(m.getName()).get());
        }).findFirst();
    }

    String[] getNetworkInterfaceIds() {
        return Arrays.stream(hal.getNetworkIFs()).map(NetworkIF::getName).toArray(String[]::new);
    }

    Optional<NetworkInterfaceSpeed> getSpeed(String id) {

        Optional<NetworkIF> networkOptional = Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).findAny();
        if (!networkOptional.isPresent()) {
            LOGGER.warn("No NIC with id %s was found", id);
            return Optional.empty();
        }
        NetworkIF networkIF = networkOptional.get();
        NetworkInterfaceSpeedMeasurement start = nicSpeedStore.get(id);

        if (start == null || utils.isOutsideSamplingDuration(start.getSampledAt(), MAX_SAMPLING_THRESHOLD)) {
            networkIF.updateNetworkStats();
            start = new NetworkInterfaceSpeedMeasurement(networkIF.getBytesRecv(),
                    networkIF.getBytesSent(),
                    utils.currentSystemTime());
            LOGGER.info("Sleeping thread since we don't have enough sample data. Hold on!");
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }

        nicSpeedStore.remove(id);

        networkIF.updateNetworkStats();
        NetworkInterfaceSpeedMeasurement end = new NetworkInterfaceSpeedMeasurement(networkIF.getBytesRecv(),
                networkIF.getBytesSent(),
                utils.currentSystemTime());
        nicSpeedStore.put(id, end);

        final long rxbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getRxBytes(), end.getRxBytes());
        final long txbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getTxBytes(), end.getTxBytes());

        return Optional.of(new NetworkInterfaceSpeed(rxbps, txbps));
    }

    private long measureSpeed(long start, long end, long rxBytesStart, long rxBytesEnd) {
        return (rxBytesEnd - rxBytesStart) * BYTE_TO_BIT / (end - start) * SLEEP_SAMPLE_PERIOD;
    }

}
