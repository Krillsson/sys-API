package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

public class MonitorConfiguration {
    @Valid
    @JsonProperty
    private long interval;
    @Valid
    @JsonProperty
    private TimeUnit unit;

    public MonitorConfiguration(long interval, TimeUnit unit) {
        this.interval = interval;
        this.unit = unit;
    }

    public MonitorConfiguration() {
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
