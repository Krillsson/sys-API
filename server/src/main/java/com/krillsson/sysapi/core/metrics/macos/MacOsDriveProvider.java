package com.krillsson.sysapi.core.metrics.macos;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.drives.DriveValues;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveProvider;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MacOsDriveProvider extends DefaultDriveProvider {

    private static final String FIRST_SECTOR_ON_DRIVE = "s1";
    private static final String ROOT_MOUNT = "/";

    protected MacOsDriveProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
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

                Optional<DriveLoad> firstHalf = driveLoads.stream()
                        .max(Comparator.comparingInt(o -> (int) o.getValues().getUsableSpace()));
                Optional<DriveLoad> secondHalf = driveLoads.stream()
                        .max(Comparator.comparingInt(o -> (int) o.getValues().getReads()));


                if (secondHalf.isPresent() && firstHalf.isPresent()) {
                    DriveLoad first = firstHalf.get();
                    DriveLoad second = secondHalf.get();
                    DriveValues values = new DriveValues(
                            first.getValues().getUsableSpace(),
                            first.getValues().getTotalSpace(),
                            first.getValues().getOpenFileDescriptors(),
                            first.getValues().getMaxFileDescriptors(),
                            second.getValues().getReads(),
                            second.getValues().getReadBytes(),
                            second.getValues().getWrites(),
                            second.getValues().getWriteBytes()
                    );
                    DriveLoad driveLoad = new DriveLoad(
                            second.getName(),
                            first.getSerial(),
                            values,
                            second.getSpeed(),
                            second.getHealth()
                    );
                    result.add(driveLoad);
                } else {
                    firstHalf.ifPresent(result::add);
                    secondHalf.ifPresent(result::add);
                }
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
