package com.krillsson.sysapi.core.macos;

import com.krillsson.sysapi.core.DefaultDiskProvider;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MacDiskProvider extends DefaultDiskProvider {

    public MacDiskProvider(OperatingSystem operatingSystem, HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(operatingSystem, hal, speedMeasurementManager);
    }

    /**
     * Workaround for there being multiple entries for same Disk. When using APFS(?)
     *
     * @return
     */
    @Override
    protected StorageInfo diskInfos() {
        StorageInfo storageInfo = super.diskInfos();
        fixDuplicateDrive(storageInfo);
        return storageInfo;
    }

    private void fixDuplicateDrive(StorageInfo storageInfo) {
        List<DiskInfo> duplicates = findDuplicates(storageInfo);
        if (duplicates.size() == 2) {

            DiskInfo legacy = duplicates.get(0);
            DiskInfo apfs = duplicates.get(1);

            HWDiskStore mergedHwDiskStore = legacy.getHwDiskStore();
            mergedHwDiskStore.setName(apfs.getHwDiskStore().getName());
            mergedHwDiskStore.setPartitions(fixPartitionSizes(apfs.getHwDiskStore().getSize(), apfs.getHwDiskStore().getPartitions()));
            DiskInfo mergedDiskInfo = new DiskInfo(mergedHwDiskStore, legacy.getDiskHealth(), legacy.getDiskSpeed(), apfs.getOsFileStore());
            List<DiskInfo> newList = new ArrayList<DiskInfo>(Arrays.asList(storageInfo.getDiskInfo()));
            newList.remove(legacy);
            newList.remove(apfs);
            newList.add(mergedDiskInfo);
            storageInfo.setDiskInfo(newList.toArray(/*type reference*/new DiskInfo[]{}));
        }
    }

    private List<DiskInfo> findDuplicates(StorageInfo storageInfo) {
        return Stream.of(storageInfo.getDiskInfo())
                .collect(Collectors.groupingBy(d -> d.getHwDiskStore().getSerial()))
                .values()
                .stream()
                .filter(duplicates -> duplicates.size() == 2)
                .findAny()
                .orElse(Collections.emptyList())
                .stream()
                // this will always put apfs at second place
                .sorted(Comparator.comparingLong(d -> -d.getHwDiskStore().getReadBytes()))
                .collect(Collectors.toList());
    }

    /**
     * Fixes is maybe a wrong name. This method just restores the weirdness of APFS partitions
     * if the partitions size are equally large as the total volume only the first partition is
     * keeping it's size. I can imagine that this will cause weirdness.
     *
     * @param totalSize
     * @param resultingPartitions
     * @return
     */
    private HWPartition[] fixPartitionSizes(long totalSize, HWPartition[] resultingPartitions) {
        for (HWPartition resultingPartition : resultingPartitions) {
            totalSize -= resultingPartition.getSize();
            if (totalSize < 0) {
                resultingPartition.setSize(0);
            }
        }
        return resultingPartitions;
    }
}
