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

import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.DiskSpeed;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultDiskProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultDiskProvider.class);

    private final OperatingSystem operatingSystem;
    private final HardwareAbstractionLayer hal;
    private final SpeedMeasurementManager speedMeasurementManager;

    DefaultDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        this.operatingSystem = operatingSystem;
        this.hal = hal;
        this.speedMeasurementManager = speedMeasurementManager;
        register();
    }

    private void register(){
        for (HWDiskStore store : hal.getDiskStores()) {
            speedMeasurementManager.register(new DiskSpeedSource(store.getName(), hal));
        }
    }

    StorageInfo storageInfo() {
        List<DiskInfo> diskInfos = new ArrayList<>();
        for (HWDiskStore diskStore : hal.getDiskStores()) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            String name = associatedFileStore != null ? associatedFileStore.getName() : "N/A";
            diskInfos.add(new DiskInfo(diskStore, diskHealth(name), diskSpeedForName(diskStore.getName()), associatedFileStore));
        }

        FileSystem fileSystem = operatingSystem.getFileSystem();
        return new StorageInfo(diskInfos.toArray(/*type reference*/new DiskInfo[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors());
    }

    Optional<DiskInfo> getDiskInfoByName(String name) {
        return Arrays.stream(hal.getDiskStores()).filter(d -> d.getName().equals(name)).map(di -> {
            OSFileStore associatedFileStore = findAssociatedFileStore(di);
            String mount = associatedFileStore != null ? associatedFileStore.getName() : "N/A";
            return new DiskInfo(di, diskHealth(mount), diskSpeedForName(di.getName()), associatedFileStore);
        }).findFirst();
    }

    private DiskSpeed diskSpeedForName(String name){
        SpeedMeasurementManager.CurrentSpeed currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(name);
        return new DiskSpeed(currentSpeedForName.getReadPerSeconds(), currentSpeedForName.getWritePerSeconds());
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

    private static class DiskSpeedSource implements SpeedMeasurementManager.SpeedSource {

        private final String name;
        private final HardwareAbstractionLayer hal;

        private DiskSpeedSource(String name, HardwareAbstractionLayer hal) {
            this.name = name;
            this.hal = hal;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getCurrentRead() {
            return Arrays.stream(hal.getDiskStores())
                    .filter(d -> d.getName().equals(name))
                    .map(HWDiskStore::getReadBytes)
                    .findAny()
                    .orElse(0L);
        }

        @Override
        public long getCurrentWrite() {
            return Arrays.stream(hal.getDiskStores())
                    .filter(d -> d.getName().equals(name))
                    .map(HWDiskStore::getWriteBytes)
                    .findAny()
                    .orElse(0L);
        }
    }
}
