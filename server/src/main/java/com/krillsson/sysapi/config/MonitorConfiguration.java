package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MonitorConfiguration {
    @Valid
    @NotNull
    @JsonProperty
    private long inertia;
    @Valid
    @NotNull
    @JsonProperty
    private ChronoUnit unit;

    public MonitorConfiguration(long inertia, ChronoUnit unit) {
        this.inertia = inertia;
        this.unit = unit;
    }

    public MonitorConfiguration() {
    }

    public long getInertia() {
        return inertia;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public Duration duration()
    {
        return Duration.of(getInertia(), getUnit());
    }
}
