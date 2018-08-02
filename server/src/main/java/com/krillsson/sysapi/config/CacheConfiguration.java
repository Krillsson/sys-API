package com.krillsson.sysapi.config;

import java.util.concurrent.TimeUnit;

public class CacheConfiguration {
    private long duration;
    private TimeUnit unit;

    public CacheConfiguration(long duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public CacheConfiguration() {
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
