package se.christianjensen.maintenance.representation.internal;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {

    @JsonProperty
    private long id;

    @JsonProperty
    private String gcmId;

    @JsonProperty
    private long pushTimestamp;


    public Device() {
    }

    public Device(long id,
                  String gcmId) {
        this.id = id;
        this.gcmId = gcmId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;

        if (gcmId != null) {
            this.pushTimestamp = System.currentTimeMillis();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPushTimestamp() {
        return pushTimestamp;
    }

    public boolean isRegistered() {
        return gcmId != null;
    }
}
