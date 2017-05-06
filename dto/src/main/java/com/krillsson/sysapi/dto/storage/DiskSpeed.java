package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "readBytesPerSecond",
        "writeBytesPerSecond"
})
public class DiskSpeed {

    @JsonProperty("readBytesPerSecond")
    private long readBytesPerSecond;

    @JsonProperty("writeBytesPerSecond")
    private long writeBytesPerSecond;

    /**
     * No args constructor for use in serialization
     */
    public DiskSpeed() {
    }

    /**
     * @param readBytesPerSecond
     * @param writeBytesPerSecond
     */
    public DiskSpeed(long readBytesPerSecond, long writeBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
        this.writeBytesPerSecond = writeBytesPerSecond;
    }

    @JsonProperty("readBytesPerSecond")
    public long getReadBytesPerSecond() {
        return readBytesPerSecond;
    }

    @JsonProperty("readBytesPerSecond")
    public void setReadBytesPerSecond(long readBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
    }

    @JsonProperty("writeBytesPerSecond")
    public long getWriteBytesPerSecond() {
        return writeBytesPerSecond;
    }

    @JsonProperty("writeBytesPerSecond")
    public void setWriteBytesPerSecond(long writeBytesPerSecond) {
        this.writeBytesPerSecond = writeBytesPerSecond;
    }
}
