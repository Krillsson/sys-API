package se.christianjensen.maintenance.representation.memory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by christian on 2014-11-30.
*/
public abstract class MemSegment {
    protected final long total;
    protected final long used;
    protected final long free;

    public MemSegment(long total, long used, long free) {
        this.total = total;
        this.used = used;
        this.free = free;
    }

    @JsonProperty
    public long total() { return total; }

    @JsonProperty
    public long used() { return used; }

    @JsonProperty
    public long free() { return free; }
}
