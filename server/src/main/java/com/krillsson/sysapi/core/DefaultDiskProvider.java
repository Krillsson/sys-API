/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.storage.DiskSpeed;
import com.krillsson.sysapi.core.domain.storage.DiskSpeedMeasurement;
import com.krillsson.sysapi.core.domain.storage.*;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.*;
import java.util.concurrent.TimeUnit;

class DefaultDiskProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultDiskProvider.class);
    private static final double MILLIS_TO_SECONDS = 1000.0;

    private HashMap<String, DiskSpeedMeasurement> diskSpeedStore = new HashMap<>();

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final long SLEEP_SAMPLE_PERIOD = TimeUnit.SECONDS.toMillis(2);

    private final OperatingSystem operatingSystem;
    private final HardwareAbstractionLayer hal;
    private final Utils utils;

    DefaultDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, Utils utils) {
        this.operatingSystem = operatingSystem;
        this.hal = hal;
        this.utils = utils;
    }

    StorageInfo storageInfo() {
        List<DiskInfo> diskInfos = new ArrayList<>();
        for (HWDiskStore diskStore : hal.getDiskStores()) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            String name = associatedFileStore != null ? associatedFileStore.getMount() : "N/A";
            diskInfos.add(new DiskInfo(diskStore, diskHealth(name), new DiskSpeed(0,0), associatedFileStore));
        }
        FileSystem fileSystem = operatingSystem.getFileSystem();
        return new StorageInfo(diskInfos.toArray(/*type reference*/new DiskInfo[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors(), utils.currentSystemTime());
    }

    Optional<DiskInfo> getDiskInfoByName(String name){
        return Arrays.stream(hal.getDiskStores()).filter(d -> d.getName().equals(name)).map(di -> {
            OSFileStore associatedFileStore = findAssociatedFileStore(di);
            String mount = associatedFileStore != null ? associatedFileStore.getMount() : "N/A";
            return new DiskInfo(di, diskHealth(mount), new DiskSpeed(0,0), associatedFileStore);
        }).findFirst();
    }

    DiskHealth diskHealth(String name) {
        return null;
    }

    private OSFileStore findAssociatedFileStore(HWDiskStore diskStore) {
        for (OSFileStore osFileStore : Arrays.asList(operatingSystem.getFileSystem().getFileStores())) {
            for (HWPartition hwPartition : Arrays.asList(diskStore.getPartitions())) {
                if (osFileStore.getUUID().equals(hwPartition.getUuid())) {
                    return osFileStore;
                }
            }
        }
        return null;
    }
    private NetworkInterfaceData populate(HWDiskStore hWDiskStore){
        initializeMeasurement(hWDiskStore, true);
        DiskSpeed diskSpeed = measureDiskSpeed(hWDiskStore);
        return new NetworkInterfaceData(hWDiskStore, diskSpeed);
    }

    private NetworkInterfaceData[] populate(HWDiskStore[] hWDiskStoreS) {
        List<NetworkInterfaceData> interfaces = new ArrayList<>();
        for (int i = 0; i < hWDiskStoreS.length; i++) {
            HWDiskStore hWDiskStore = hWDiskStoreS[i];
            boolean lastItem = (hWDiskStoreS.length - 1) == i;
            initializeMeasurement(hWDiskStore, lastItem);
        }
        for (HWDiskStore hWDiskStore : hWDiskStoreS) {
            interfaces.add(new NetworkInterfaceData(hWDiskStore, measureDiskSpeed(hWDiskStore)));
        }
        return interfaces.toArray(new NetworkInterfaceData[0]);
    }

    private void initializeMeasurement(HWDiskStore hWDiskStore, boolean sleep) {
        boolean updateNeeded;
        DiskSpeedMeasurement start = diskSpeedStore.get(hWDiskStore.getName());
        updateNeeded = updateNeeded(start);
        if (updateNeeded) {
            diskSpeedStore.put(hWDiskStore.getName(), new DiskSpeedMeasurement(hWDiskStore.getReadBytes(),
                    hWDiskStore.getWriteBytes(),
                    utils.currentSystemTime()));
        }
        if (sleep && updateNeeded) {
            LOGGER.info("Sleeping thread. Hold on!");
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }
    }

    private DiskSpeed measureDiskSpeed(HWDiskStore hWDiskStore) {
        DiskSpeedMeasurement start = diskSpeedStore.get(hWDiskStore.getName());
        DiskSpeedMeasurement end = new DiskSpeedMeasurement(hWDiskStore.getReadBytes(),
                hWDiskStore.getWriteBytes(),
                utils.currentSystemTime());
        diskSpeedStore.put(hWDiskStore.getName(), end);
        final long rxbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getRBps(), end.getRBps());
        final long txbps = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getWBps(), end.getWBps());
        return new DiskSpeed(rxbps, txbps);
    }

    private long measureSpeed(long start, long end, long bytesStart, long bytesEnd) {
        double deltaBytes = (bytesEnd - bytesStart);
        double deltaSeconds = (end - start) / MILLIS_TO_SECONDS;
        if(deltaBytes <= 0 || deltaSeconds <= 0){
            return 0L;
        }
        double bitsPerSecond = deltaBytes / deltaSeconds;
        return (long)bitsPerSecond;
    }

    private boolean updateNeeded(DiskSpeedMeasurement start) {
        return start == null ||
                utils.isOutsideMaximumDuration(start.getSampledAt(), MAX_SAMPLING_THRESHOLD);
    }
}
