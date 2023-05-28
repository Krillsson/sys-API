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

import com.krillsson.sysapi.core.domain.drives.DriveHealth;
import com.krillsson.sysapi.core.domain.drives.DriveSpeed;
import com.krillsson.sysapi.core.domain.drives.OsPartition;
import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.metrics.Empty;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveMetrics;
import com.krillsson.sysapi.util.Streams;
import ohmwrapper.DriveMonitor;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsDriveMetrics extends DefaultDriveMetrics {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsDriveMetrics.class);

    private DelegatingOHMManager monitorManager;

    public WindowsDriveMetrics(OperatingSystem operatingSystem, HardwareAbstractionLayer hal) {
        super(operatingSystem, hal);
    }

    public void setMonitorManager(DelegatingOHMManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    @Override
    protected Optional<DriveSpeed> diskSpeedForStore(HWDiskStore diskStore, OsPartition osFileStore) {
        if (osFileStore == null) {
            return Optional.empty();
        }
        monitorManager.update();

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
        return Optional.of(new DriveSpeed((long) readRate, (long) writeRate));
    }

    @Override
    public DriveHealth diskHealth(String name) {
        monitorManager.update();
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
                         return new DriveHealth(nullSafeGetValue(dm.getTemperature()), healthDatas);

                     }
                ).findAny().orElse(Empty.DRIVE_HEALTH);
    }
}
