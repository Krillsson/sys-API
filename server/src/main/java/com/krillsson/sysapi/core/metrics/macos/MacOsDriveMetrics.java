package com.krillsson.sysapi.core.metrics.macos;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.drives.DriveSpeed;
import com.krillsson.sysapi.core.domain.drives.DriveValues;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveMetrics;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MacOsDriveMetrics extends DefaultDriveMetrics {

    private static final String FIRST_SECTOR_ON_DRIVE = "s1";
    private static final String ROOT_MOUNT = "/";

    protected MacOsDriveMetrics(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(operatingSystem, hal, speedMeasurementManager);
    }

    @Override
    public List<DriveLoad> driveLoads() {

        //fix for a drive migrated to APFS having multiple entries
        List<DriveLoad> result = new ArrayList<>();
        Map<String, List<DriveLoad>> map = super.driveLoads()
                .stream()
                .collect(Collectors.groupingBy(DriveLoad::getSerial, HashMap::new, toList()));

        map.forEach((s, driveLoads) -> {
            if (driveLoads.size() > 1) {

                DriveValues first = driveLoads.get(0).getValues();
                DriveValues second = driveLoads.get(1).getValues();

                DriveSpeed speed = first.getReads() > second.getReads() ? driveLoads.get(0).getSpeed() : driveLoads.get(
                        1).getSpeed();

                DriveValues values = new DriveValues(
                        Math.max(first.getUsableSpace(), second.getUsableSpace()),
                        Math.max(first.getTotalSpace(), second.getTotalSpace()),
                        Math.max(first.getOpenFileDescriptors(), second.getOpenFileDescriptors()),
                        Math.max(first.getMaxFileDescriptors(), second.getMaxFileDescriptors()),
                        Math.max(first.getReads(), second.getReads()),
                        Math.max(first.getReadBytes(), second.getReadBytes()),
                        Math.max(first.getWrites(), second.getWrites()),
                        Math.max(first.getWriteBytes(), second.getWriteBytes())
                );
                DriveLoad driveLoad = new DriveLoad(
                        driveLoads.get(0).getName(),
                        driveLoads.get(0).getSerial(),
                        values,
                        speed,
                        driveLoads.get(0).getHealth()
                );
                result.add(driveLoad);

            } else {
                result.add(driveLoads.get(0));
            }
        });

        return result;
    }

    @Override
    public List<Drive> drives() {
        List<Drive> result = new ArrayList<>();

        HashMap<String, List<Drive>> map = super.drives()
                .stream()
                .collect(Collectors.groupingBy(Drive::getSerial, HashMap::new, toList()));

        map.forEach((s, driveLoads) -> {
            if (driveLoads.size() > 1) {
                driveLoads.stream()
                        .max(Comparator.comparingInt(o -> (int) o.getDiskOsPartition().getUsableSpace()))
                        .ifPresent(result::add);
            } else {
                result.add(driveLoads.get(0));
            }
        });

        return result;
    }



    @Override
    protected Optional<String> pickMostSuitableOsPartition(Map<String, ? extends HWPartition> hwPartitions, Map<String, ? extends OSFileStore> osStores) {
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
