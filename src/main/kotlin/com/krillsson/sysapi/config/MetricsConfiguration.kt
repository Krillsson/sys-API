package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class MetricsConfiguration(
        @JsonProperty val history: HistoryConfiguration = HistoryConfiguration(),
        @JsonProperty val cache: CacheConfiguration = CacheConfiguration()
)