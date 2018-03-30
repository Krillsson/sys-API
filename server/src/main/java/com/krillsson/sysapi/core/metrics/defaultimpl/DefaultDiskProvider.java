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
package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.DiskMetrics;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.storage.*;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultDiskProvider implements DiskMetrics {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultDiskProvider.class);
    protected static final DiskSpeed DEFAULT_DISK_SPEED = new DiskSpeed(0, 0);
    private static final DiskOsPartition DEFAULT_OS_PART = new DiskOsPartition(
            "n/a",
            "n/a",
            "n/a",
            UUID.randomUUID().toString(),
            0,
            0,
            0,
            "n/a",
            "n/a",
            "n/a",
            "n/a",
            "n/a",
            0,
            0

    );
    public static final DiskHealth DEFAULT_DISK_HEALTH = new DiskHealth(-1, Collections.emptyList());

    private final OperatingSystem operatingSystem;
    private final HardwareAbstractionLayer hal;
    private final SpeedMeasurementManager speedMeasurementManager;

    protected DefaultDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        this.operatingSystem = operatingSystem;
        this.hal = hal;
        this.speedMeasurementManager = speedMeasurementManager;
    }

    public void register() {
        for (HWDiskStore store : hal.getDiskStores()) {
            speedMeasurementManager.register(new DiskSpeedSource(store.getName(), hal));
        }
    }

    @Override
    public List<DiskInfo> diskInfos() {
        return Stream.of(hal.getDiskStores()).map(d -> new DiskInfo(
                d.getModel(),
                d.getName(),
                d.getSerial(),
                findAssociatedFileStore(d).orElse(DEFAULT_OS_PART),
                Stream.of(d.getPartitions())
                        .map(p -> new DiskPartition(
                                p.getIdentification(),
                                p.getName(),
                                p.getType(),
                                p.getUuid(),
                                p.getSize(),
                                p.getMajor(),
                                p.getMinor(),
                                p.getMountPoint()
                        )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @Override
    public List<DiskLoad> diskLoads() {
        return Stream.of(hal.getDiskStores()).map(this::createDiskLoad).collect(Collectors.toList());
    }

    @Override
    public Optional<DiskLoad> diskLoadByName(String name) {
        return Stream.of(hal.getDiskStores())
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .map(this::createDiskLoad)
                .findAny();
    }

    protected com.krillsson.sysapi.core.domain.storage.DiskMetrics diskMetrics(HWDiskStore disk, DiskOsPartition partition, FileSystem fileSystem) {
        return new com.krillsson.sysapi.core.domain.storage.DiskMetrics(
                partition.getUsableSpace(),
                partition.getTotalSpace(),
                fileSystem.getOpenFileDescriptors(),
                fileSystem.getMaxFileDescriptors(),
                disk.getReads(),
                disk.getReadBytes(),
                disk.getWrites(),
                disk.getWriteBytes()
        );
    }


    @Override
    public Optional<DiskInfo> diskInfoByName(String name) {
        return Stream.of(hal.getDiskStores()).filter(n -> n.getName().equalsIgnoreCase(name)).map(d -> new DiskInfo(
                d.getModel(),
                d.getName(),
                d.getSerial(),
                findAssociatedFileStore(d).orElse(DEFAULT_OS_PART),
                Stream.of(d.getPartitions())
                        .map(p -> new DiskPartition(
                                p.getIdentification(),
                                p.getName(),
                                p.getType(),
                                p.getUuid(),
                                p.getSize(),
                                p.getMajor(),
                                p.getMinor(),
                                p.getMountPoint()
                        )).collect(Collectors.toList())
        )).findAny();
    }

    protected Optional<DiskSpeed> diskSpeedForStore(HWDiskStore diskStore, DiskOsPartition osFileStore) {
        Optional<SpeedMeasurementManager.CurrentSpeed> currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(
                diskStore.getName());
        return currentSpeedForName.map(s -> new DiskSpeed(
                s.getReadPerSeconds(),
                s.getWritePerSeconds()
        ));
    }

    public DiskHealth diskHealth(String name) {
        return DEFAULT_DISK_HEALTH;
    }

    private Optional<DiskOsPartition> findAssociatedFileStore(HWDiskStore diskStore) {
        for (OSFileStore osStore : Stream.of(operatingSystem.getFileSystem().getFileStores()).collect(Collectors.toList())) {
            List<HWPartition> asList = Stream.of(diskStore.getPartitions()).collect(Collectors.toList());
            for (HWPartition partition : asList) {
                if (osStore.getUUID().equalsIgnoreCase(partition.getUuid())) {
                    return Optional.of(new DiskOsPartition(
                            partition.getIdentification(),
                            partition.getName(),
                            osStore.getType(),
                            partition.getUuid(),
                            partition.getSize(),
                            partition.getMajor(),
                            partition.getMinor(),
                            osStore.getMount(),
                            osStore.getVolume(),
                            osStore.getLogicalVolume(),
                            osStore.getMount(),
                            osStore.getDescription(),
                            osStore.getUsableSpace(),
                            osStore.getTotalSpace()
                    ));
                }
            }
        }
        return Optional.empty();
    }

    private DiskLoad createDiskLoad(HWDiskStore d) {
        DiskOsPartition partition = findAssociatedFileStore(d).orElse(DEFAULT_OS_PART);
        DiskHealth health = diskHealth(d.getName());
        com.krillsson.sysapi.core.domain.storage.DiskMetrics metrics = diskMetrics(d, partition, operatingSystem.getFileSystem());
        DiskSpeed speed = diskSpeedForStore(d, partition).orElse(DEFAULT_DISK_SPEED);
        return new DiskLoad(d.getName(), metrics, speed, health);
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
