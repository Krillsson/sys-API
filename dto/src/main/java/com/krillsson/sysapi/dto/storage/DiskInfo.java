package com.krillsson.sysapi.dto.storage;

public class DiskInfo {


    private DiskStore diskStore;

    private DiskSpeed diskSpeed;

    private DiskHealth diskHealth;

    private OsFileStore osFileStore;

    /**
     * No args constructor for use in serialization
     */
    public DiskInfo() {
    }

    /**
     * @param diskStore
     * @param osFileStore
     * @param diskHealth
     */
    public DiskInfo(DiskStore diskStore, DiskHealth diskHealth, OsFileStore osFileStore) {
        super();
        this.diskStore = diskStore;
        this.diskHealth = diskHealth;
        this.osFileStore = osFileStore;
    }


    public DiskStore getDiskStore() {
        return diskStore;
    }


    public void setDiskStore(DiskStore diskStore) {
        this.diskStore = diskStore;
    }


    public DiskSpeed getDiskSpeed() {
        return diskSpeed;
    }


    public void setDiskSpeed(DiskSpeed diskSpeed) {
        this.diskSpeed = diskSpeed;
    }


    public DiskHealth getDiskHealth() {
        return diskHealth;
    }


    public void setDiskHealth(DiskHealth diskHealth) {
        this.diskHealth = diskHealth;
    }


    public OsFileStore getOsFileStore() {
        return osFileStore;
    }


    public void setOsFileStore(OsFileStore osFileStore) {
        this.osFileStore = osFileStore;
    }

}
