package com.krillsson.sysapi.domain.storage;

import oshi.json.hardware.HWDiskStore;
import oshi.json.software.os.OSFileStore;

public class HWDisk {
    private final HWDiskStore diskStore;
    private final HWDiskLoad load;
    private final HWDiskHealth health;
    private final OSFileStore associatedFileStore;

    public HWDisk(HWDiskStore hwDiskStore, HWDiskLoad load, HWDiskHealth health, OSFileStore associatedFileStore) {
        this.diskStore = hwDiskStore;
        this.load = load;
        this.health = health;
        this.associatedFileStore = associatedFileStore;
    }

    public HWDiskStore getDiskStore() {
        return diskStore;
    }

    public OSFileStore getAssociatedFileStore() {
        return associatedFileStore;
    }

    public HWDiskLoad getLoad() {
        return load;
    }

    public HWDiskHealth getHealth() {
        return health;
    }
}
