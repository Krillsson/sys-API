package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class MetricsConfiguration(
    @JsonProperty val monitor: MonitorConfiguration,
    @JsonProperty val history: HistoryConfiguration,
    @JsonProperty val cache: CacheConfiguration
)