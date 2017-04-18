package com.krillsson.sysapi.core.domain.storage;

public class DiskSpeed {
    private final long rBps;
    private final long wBps;

    public DiskSpeed(long rBps, long wBps) {
        this.rBps = rBps;
        this.wBps = wBps;
    }

    public long getRBps() {
        return rBps;
    }

    public long getWBps() {
        return wBps;
    }
}
