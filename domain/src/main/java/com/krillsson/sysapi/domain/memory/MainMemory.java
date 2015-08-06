package com.krillsson.sysapi.domain.memory;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class MainMemory extends MemSegment {
    private final long actualUsed, actualFree;
    private final double usedPercent, freePercent;

    public MainMemory(//
                       long total, long used, long free, //
                       long actualUsed, long actualFree,
                       double usedPercent, double freePercent) {
        super(total, used, free);
        this.actualUsed = actualUsed;
        this.actualFree = actualFree;
        this.usedPercent = usedPercent;
        this.freePercent = freePercent;
    }

    public static MainMemory undef() {
        return new MainMemory(-1L, -1L, -1L, -1L, -1L, -1, -1);
    }

    @JsonProperty
    public long actualUsed() { return actualUsed; }

    @JsonProperty
    public long actualFree() { return actualFree; }

    @JsonProperty
    public double usedPercent() { return usedPercent; }

    @JsonProperty
    public double freePercent() { return freePercent; }
}
