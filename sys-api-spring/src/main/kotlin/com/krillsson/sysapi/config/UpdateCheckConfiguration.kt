package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateCheckConfiguration(
    @JsonProperty var enabled: Boolean = true,
    @JsonProperty var address: String = "https://api.github.com",
    @JsonProperty var user: String = "krillsson",
    @JsonProperty var repo: String = "sys-api",
)