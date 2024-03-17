package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class SelfSignedCertificateConfiguration(
    @JsonProperty var enabled: Boolean,
    @JsonProperty var populateCN: Boolean,
    @JsonProperty var populateSAN: Boolean,
    @JsonProperty var commonName: String = "sys-api.org",
    @JsonProperty var subjectAlternativeNames: List<String> = emptyList()
)