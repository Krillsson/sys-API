package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class ConnectivityCheckConfiguration(
    @JsonProperty val enabled: Boolean,
    @JsonProperty val address: String
)