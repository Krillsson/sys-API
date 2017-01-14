package com.krillsson.sysapi.domain.storage;

import oshi.json.hardware.HWDiskStore;
import oshi.json.software.os.OSFileStore;

public class HWDisk {
    private final HWDiskStore diskStore;
    private final HWDiskHealth health;
    private final OSFileStore associatedFileStore;

    public HWDisk(HWDiskStore hwDiskStore, HWDiskHealth health, OSFileStore associatedFileStore) {
        this.diskStore = hwDiskStore;
        this.health = health;
        this.associatedFileStore = associatedFileStore;
    }

    public HWDiskStore getDiskStore() {
        return diskStore;
    }

    public HWDiskHealth getHealth() {
        return health;
    }

    public OSFileStore getAssociatedFileStore() {
        return associatedFileStore;
    }
}
