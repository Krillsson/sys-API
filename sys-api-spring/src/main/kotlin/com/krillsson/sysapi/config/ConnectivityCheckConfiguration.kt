package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class ConnectivityCheckConfiguration(
    @JsonProperty var enabled: Boolean,
    @JsonProperty var address: String
)