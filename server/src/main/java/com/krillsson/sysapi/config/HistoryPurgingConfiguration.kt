package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class HistoryPurgingConfiguration {

    @Valid
    @JsonProperty
    private int olderThan;

    @Valid
    @JsonProperty
    private ChronoUnit unit;

    @Valid
    @JsonProperty
    private long purgeEvery;

    @Valid
    @JsonProperty
    private TimeUnit purgeEveryUnit;

    public HistoryPurgingConfiguration(int olderThan, ChronoUnit unit, long purgeEvery, TimeUnit purgeEveryUnit) {
        this.olderThan = olderThan;
        this.unit = unit;
        this.purgeEvery = purgeEvery;
        this.purgeEveryUnit = purgeEveryUnit;
    }

    public HistoryPurgingConfiguration() {
    }

    public int getOlderThan() {
        return olderThan;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public long getPurgeEvery() {
        return purgeEvery;
    }

    public TimeUnit getPurgeEveryUnit() {
        return purgeEveryUnit;
    }
}
