package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class HistoryPurgingConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private int olderThan;
    @Valid
    @NotNull
    @JsonProperty
    private ChronoUnit unit;
    @Valid
    @NotNull
    @JsonProperty
    private long purgeEvery;
    @Valid
    @NotNull
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
