package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class MetricsConfiguration(
    @JsonProperty var history: HistoryConfiguration = HistoryConfiguration(),
    @JsonProperty var cache: CacheConfiguration = CacheConfiguration()
)