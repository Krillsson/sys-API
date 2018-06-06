package com.krillsson.sysapi.core.metrics.macos;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveProvider;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MacOsDriveProvider extends DefaultDriveProvider {

    private static final String FIRST_SECTOR_ON_DRIVE = "s1";
    private static final String ROOT_MOUNT = "/";

    protected MacOsDriveProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(operatingSystem, hal, speedMeasurementManager);
    }

    @Override
    protected Optional<String> pickMostSuitableOsPartition(Map<String, HWPartition> hwPartitions, Map<String, OSFileStore> osStores) {
        Set<String> strings = hwPartitions.keySet();
        /*intersection*/
        strings.retainAll(osStores.keySet());

        if (!strings.isEmpty()) {
            String[] keys = strings.toArray(new String[0]);
            if (keys.length == 1) {
                return Optional.of(keys[0]);
            } else {
                // try to find the actual main partition (should cover 99% of the cases)
                for (String key : keys) {
                    OSFileStore osFileStore = osStores.get(key);
                    if (osFileStore.getMount().equals(ROOT_MOUNT) || osFileStore.getName()
                            .endsWith(FIRST_SECTOR_ON_DRIVE)) {
                        return Optional.of(key);
                    }
                }
                //otherwise pick one at random ¯\_(ツ)_/¯
                return Optional.of(keys[0]);
            }

        } else {
            return Optional.empty();
        }
    }
}
