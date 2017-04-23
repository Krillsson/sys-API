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

class DefaultNetworkProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultNetworkProvider.class);
    private static final double MILLIS_TO_SECONDS = 1000.0;

    private HashMap<String, SpeedMeasurement> nicSpeedStore = new HashMap<>();

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final long SLEEP_SAMPLE_PERIOD = TimeUnit.SECONDS.toMillis(2);
    private static final int BYTE_TO_BIT = 8;

    private final HardwareAbstractionLayer hal;
    private final Utils utils;

    DefaultNetworkProvider(HardwareAbstractionLayer hal, Utils utils) {
        this.hal = hal;
        this.utils = utils;
    }

    String[] getNetworkInterfaceNames(){
        return Arrays.stream(hal.getNetworkIFs()).map(NetworkIF::getName).toArray(String[]::new);
    }

    NetworkInterfaceData[] getAllNetworkInterfaces() {
        return populate(hal.getNetworkIFs());
    }

    Optional<NetworkInterfaceData> getNetworkInterfaceById(String id) {
        return Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).map(this::populate).findFirst();
    }

    Optional<NetworkInterfaceSpeed> getSpeed(String id) {
        Optional<NetworkIF> networkOptional = Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).findAny();
        if (networkOptional.isPresent()) {
            NetworkIF networkIF = networkOptional.get();
            return Optional.of(populate(networkIF).getNetworkInterfaceSpeed());
        } else {
            return Optional.empty();
        }
    }

    private NetworkInterfaceData populate(NetworkIF networkIF){
        initializeMeasurement(networkIF, true);
        NetworkInterfaceSpeed networkInterfaceSpeed = measureNetworkSpeed(networkIF);
        return new NetworkInterfaceData(networkIF, networkInterfaceSpeed);
    }

    private NetworkInterfaceData[] populate(NetworkIF[] networkIFS) {
        List<NetworkInterfaceData> interfaces = new ArrayList<>();
        for (int i = 0; i < networkIFS.length; i++) {
            NetworkIF networkIF = networkIFS[i];
            boolean lastItem = (networkIFS.length - 1) == i;
            initializeMeasurement(networkIF, lastItem);
        }
        for (NetworkIF networkIF : networkIFS) {
            interfaces.add(new NetworkInterfaceData(networkIF, measureNetworkSpeed(networkIF)));
        }
        return interfaces.toArray(new NetworkInterfaceData[0]);
    }

    private void initializeMeasurement(NetworkIF networkIF, boolean sleep) {
        boolean updateNeeded;
        SpeedMeasurement start = nicSpeedStore.get(networkIF.getName());
        updateNeeded = updateNeeded(start);
        if (updateNeeded) {
            networkIF.updateNetworkStats();
            nicSpeedStore.put(networkIF.getName(), new SpeedMeasurement(networkIF.getBytesRecv(),
                    networkIF.getBytesSent(),
                    utils.currentSystemTime()));
        }
        if (sleep && updateNeeded) {
            LOGGER.info("Sleeping thread. Hold on!");
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }
    }

    private NetworkInterfaceSpeed measureNetworkSpeed(NetworkIF networkIF) {
        SpeedMeasurement start = nicSpeedStore.get(networkIF.getName());
        networkIF.updateNetworkStats();
        SpeedMeasurement end = new SpeedMeasurement(networkIF.getBytesRecv(),
                networkIF.getBytesSent(),
                utils.currentSystemTime());
        nicSpeedStore.put(networkIF.getName(), end);
        final long rxbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getRead(), end.getRead());
        final long txbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getWrite(), end.getWrite());
        return new NetworkInterfaceSpeed(rxbps, txbps);
    }

    private long measureSpeed(long start, long end, long bytesStart, long bytesEnd) {
        double deltaBits = (bytesEnd - bytesStart) * BYTE_TO_BIT;
        double deltaSeconds = (end - start) / MILLIS_TO_SECONDS;
        if(deltaBits <= 0 || deltaSeconds <= 0){
            return 0L;
        }
        double bitsPerSecond = deltaBits / deltaSeconds;
        return (long)bitsPerSecond;
    }

    private boolean updateNeeded(SpeedMeasurement start) {
        return start == null ||
                utils.isOutsideMaximumDuration(start.getSampledAt(), MAX_SAMPLING_THRESHOLD);
    }

}
