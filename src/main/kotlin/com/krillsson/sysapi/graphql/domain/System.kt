package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform

data class System(
        val hostName: String,
        val operatingSystem: OperatingSystem,
        val platform: Platform
)