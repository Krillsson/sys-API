package com.krillsson.sysapi.domain.storage;

public class StorageInfo {
    private final HWDisk[] HWDisks;
    private final long openFileDescriptors;
    private final long maxFileDescriptors;

    public StorageInfo(HWDisk[] HWDisks, long openFileDescriptors, long maxFileDescriptors) {
        this.HWDisks = HWDisks;
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
    }

    public HWDisk[] getDiskStores() {
        return HWDisks;
    }

    public long getOpenFileDescriptors() {
        return openFileDescriptors;
    }

    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }
}
