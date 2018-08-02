package com.krillsson.sysapi.dto.storage;

public class OsFileStore {


    private String name;

    private String volume;

    private String mount;

    private String description;

    private String uuid;

    private long usableSpace;

    private long totalSpace;

    private String type;

    /**
     * No args constructor for use in serialization
     */
    public OsFileStore() {
    }

    /**
     * @param totalSpace
     * @param usableSpace
     * @param mount
     * @param description
     * @param name
     * @param volume
     * @param uuid
     * @param type
     */
    public OsFileStore(String name, String volume, String mount, String description, String uuid, long usableSpace, long totalSpace, String type) {
        super();
        this.name = name;
        this.volume = volume;
        this.mount = mount;
        this.description = description;
        this.uuid = uuid;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
        this.type = type;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getVolume() {
        return volume;
    }


    public void setVolume(String volume) {
        this.volume = volume;
    }


    public String getMount() {
        return mount;
    }


    public void setMount(String mount) {
        this.mount = mount;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getuuid() {
        return uuid;
    }


    public void setuuid(String uuid) {
        this.uuid = uuid;
    }


    public long getUsableSpace() {
        return usableSpace;
    }


    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }


    public long getTotalSpace() {
        return totalSpace;
    }


    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

}
