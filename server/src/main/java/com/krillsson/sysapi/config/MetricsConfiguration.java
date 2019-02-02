package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MetricsConfiguration {

    @Valid
    @JsonProperty
    private MonitorConfiguration monitor;

    @Valid
    @JsonProperty
    private HistoryConfiguration history;
    @Valid
    @JsonProperty
    private CacheConfiguration cache;

    public MetricsConfiguration(MonitorConfiguration monitor, HistoryConfiguration history, CacheConfiguration cache) {
        this.monitor = monitor;
        this.history = history;
        this.cache = cache;
    }

    public MetricsConfiguration() {
    }

    public MonitorConfiguration getMonitor() {
        return monitor;
    }

    public HistoryConfiguration getHistory() {
        return history;
    }

    public CacheConfiguration getCache() {
        return cache;
    }
}
