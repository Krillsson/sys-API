package com.krillsson.sysapi.domain.storage;

import oshi.json.software.os.FileSystem;

public class Storage {
    private final Disk[] diskStores;
    private final long openFileDescriptors;
    private final long maxFileDescriptors;

    public Storage(Disk[] diskStores, long openFileDescriptors, long maxFileDescriptors) {
        this.diskStores = diskStores;
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
    }

    public Disk[] getDiskStores() {
        return diskStores;
    }

    public long getOpenFileDescriptors() {
        return openFileDescriptors;
    }

    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }
}
