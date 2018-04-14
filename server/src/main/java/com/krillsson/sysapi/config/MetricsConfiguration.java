package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MetricsConfiguration {
    @Valid
    @NotNull
    @JsonProperty
    private HistoryConfiguration history;
    @Valid
    @NotNull
    @JsonProperty
    private CacheConfiguration cache;

    public MetricsConfiguration(HistoryConfiguration history, CacheConfiguration cache) {
        this.history = history;
        this.cache = cache;
    }

    public MetricsConfiguration() {
    }

    public HistoryConfiguration getHistory() {
        return history;
    }

    public CacheConfiguration getCache() {
        return cache;
    }
}
