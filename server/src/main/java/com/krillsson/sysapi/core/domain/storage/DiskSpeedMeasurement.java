package com.krillsson.sysapi.core.domain.storage;

public class DiskSpeedMeasurement {
    private final long rBps;
    private final long wBps;
    private final long sampledAt;

    public DiskSpeedMeasurement(long rBps, long wBps, long sampledAt) {
        this.rBps = rBps;
        this.wBps = wBps;
        this.sampledAt = sampledAt;
    }

    public long getRBps() {
        return rBps;
    }

    public long getWBps() {
        return wBps;
    }

    public long getSampledAt() {
        return sampledAt;
    }
}
