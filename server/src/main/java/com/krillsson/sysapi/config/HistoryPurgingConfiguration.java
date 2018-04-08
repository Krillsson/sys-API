package com.krillsson.sysapi.config;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class HistoryPurgingConfiguration {

    private int olderThan;
    private ChronoUnit olderThanUnit;
    private long purgeEveryInitialDelay;
    private long purgeEveryDelay;
    private TimeUnit purgeEveryUnit;

    public int getOlderThan() {
        return olderThan;
    }

    public ChronoUnit getOlderThanUnit() {
        return olderThanUnit;
    }

    public long getPurgeEveryInitialDelay() {
        return purgeEveryInitialDelay;
    }

    public long getPurgeEveryDelay() {
        return purgeEveryDelay;
    }

    public TimeUnit getPurgeEveryUnit() {
        return purgeEveryUnit;
    }
}
