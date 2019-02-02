package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

public class HistoryConfiguration {
    @Valid
    @JsonProperty
    private HistoryPurgingConfiguration purging;

    @Valid
    @JsonProperty
    private long interval;

    @Valid
    @JsonProperty
    private TimeUnit unit;

    public HistoryConfiguration(HistoryPurgingConfiguration purging, long interval, TimeUnit unit) {
        this.purging = purging;
        this.interval = interval;
        this.unit = unit;
    }

    public HistoryConfiguration() {
    }

    public HistoryPurgingConfiguration getPurging() {
        return purging;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
