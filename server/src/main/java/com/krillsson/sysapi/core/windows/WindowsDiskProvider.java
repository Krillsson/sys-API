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
    private HardwareAbstractionLayer hal;

    public void setMonitorManager(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    public WindowsDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(operatingSystem, hal, speedMeasurementManager);
        this.hal = hal;
    }

    @Override
    protected DiskSpeed diskSpeedForName(HWDiskStore diskStore, OSFileStore osFileStore) {
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

    @Override
    protected void register() {
        // don't
    }
}
