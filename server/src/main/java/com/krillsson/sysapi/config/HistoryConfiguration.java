package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

public class HistoryConfiguration {
    @Valid
    @NotNull
    @JsonProperty
    private HistoryPurgingConfiguration purging;
    @Valid
    @NotNull
    @JsonProperty
    private long duration;
    @Valid
    @NotNull
    @JsonProperty
    private TimeUnit unit;

    public HistoryConfiguration(HistoryPurgingConfiguration purging, long duration, TimeUnit unit) {
        this.purging = purging;
        this.duration = duration;
        this.unit = unit;
    }

    public HistoryConfiguration() {
    }

    public HistoryPurgingConfiguration getPurging() {
        return purging;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
