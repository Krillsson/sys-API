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
package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDiskProvider;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import com.krillsson.sysapi.core.domain.storage.DiskOsPartition;
import com.krillsson.sysapi.core.domain.storage.DiskSpeed;
import com.krillsson.sysapi.util.Streams;
import ohmwrapper.DriveMonitor;
import ohmwrapper.MonitorManager;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsDiskProvider extends DefaultDiskProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsDiskProvider.class);

    private MonitorManager monitorManager;

    public void setMonitorManager(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    public WindowsDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(operatingSystem, hal, speedMeasurementManager);
    }

    @Override
    protected Optional<DiskSpeed> diskSpeedForStore(HWDiskStore diskStore, DiskOsPartition osFileStore) {
        if (osFileStore == null) {
            return Optional.empty();
        }
        monitorManager.Update();

        Optional<DriveMonitor> diskOptional = Arrays.stream(monitorManager.DriveMonitors())
                .filter(d -> osFileStore.getMount().equalsIgnoreCase(d.getLogicalName()))
                .findAny();
        if (!diskOptional.isPresent()) {
            LOGGER.warn("No disk with id {} was found", diskStore.getName());
            return Optional.empty();
        }

        DriveMonitor driveInfo = diskOptional.get();
        double writeRate = driveInfo.getWriteRate();
        double readRate = driveInfo.getReadRate();
        return Optional.of(new DiskSpeed((long) readRate, (long) writeRate));
    }

    @Override
    public DiskHealth diskHealth(String name) {
        monitorManager.Update();
        return Streams.ofNullable(monitorManager.DriveMonitors())
                .filter(d -> name.equalsIgnoreCase(d.getLogicalName()))
                .map(dm -> {
                         HealthData healthData = new HealthData(
                                 "Remaining life",
                                 nullSafeGetValue(dm.getRemainingLife()),
                                 DataType.PERCENT
                         );
                         List<HealthData> healthDatas = Streams.ofNullable(dm.getLifecycleData())
                                 .map(h -> new HealthData(
                                         h.getLabel(),
                                         h.getValue(),
                                         DataType.valueOf(h.getDataType().toString().toUpperCase())
                                 ))
                                 .collect(Collectors.toList());
                         healthDatas.add(healthData);
                         return new DiskHealth(nullSafeGetValue(dm.getTemperature()), healthDatas);

                     }
                ).findAny().orElse(DEFAULT_DISK_HEALTH);
    }
}
