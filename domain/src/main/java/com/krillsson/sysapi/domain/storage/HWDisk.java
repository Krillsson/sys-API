package com.krillsson.sysapi.domain.storage;

import oshi.json.hardware.HWDiskStore;
import oshi.json.software.os.OSFileStore;

public class HWDisk {
    private final HWDiskStore hwDiskStore;
    private final HWDiskHealth hwDiskHealth;
    private final OSFileStore associatedFileStore;

    public HWDisk(HWDiskStore hwDiskStore, HWDiskHealth hwDiskHealth, OSFileStore associatedFileStore) {
        this.hwDiskStore = hwDiskStore;
        this.hwDiskHealth = hwDiskHealth;
        this.associatedFileStore = associatedFileStore;
    }

    public HWDiskStore getHwDiskStore() {
        return hwDiskStore;
    }

    public HWDiskHealth getHwDiskHealth() {
        return hwDiskHealth;
    }

    public OSFileStore getAssociatedFileStore() {
        return associatedFileStore;
    }
}
