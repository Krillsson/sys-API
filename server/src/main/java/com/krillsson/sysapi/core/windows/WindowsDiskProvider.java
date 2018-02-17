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
package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultDiskProvider;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.storage.DiskSpeed;
import ohmwrapper.DriveMonitor;
import ohmwrapper.MonitorManager;
import org.slf4j.Logger;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.Optional;

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
    protected DiskSpeed diskSpeedForStore(HWDiskStore diskStore, OSFileStore osFileStore) {
        if(osFileStore == null){
            return DEFAULT_DISK_SPEED;
        }
        monitorManager.Update();

        Optional<DriveMonitor> diskOptional = Arrays.stream(monitorManager.DriveMonitors()).filter(d -> osFileStore.getMount().equalsIgnoreCase(d.getLogicalName())).findAny();
        if (!diskOptional.isPresent()) {
            LOGGER.warn("No disk with id {} was found", diskStore.getName());
            return DEFAULT_DISK_SPEED;
        }

        DriveMonitor driveInfo = diskOptional.get();
        double writeRate = driveInfo.getWriteRate();
        double readRate = driveInfo.getReadRate();
        return new DiskSpeed((long) readRate, (long) writeRate);
    }
}
