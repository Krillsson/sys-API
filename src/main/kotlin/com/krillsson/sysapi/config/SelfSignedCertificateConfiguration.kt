package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class SelfSignedCertificateConfiguration(
        @JsonProperty val enabled: Boolean,
        @JsonProperty val populateCN: Boolean,
        @JsonProperty val populateSAN: Boolean,
        @JsonProperty val commonName: String = "sys-api.org",
        @JsonProperty val subjectAlternativeNames: List<String> = emptyList()
)