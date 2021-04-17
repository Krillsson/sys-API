package com.krillsson.sysapi.util

import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import oshi.PlatformEnum
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun oshi.software.os.OperatingSystem.asOperatingSystem(): OperatingSystem {
    return OperatingSystem(
        manufacturer,
        family,
        OperatingSystem.VersionInfo(
            versionInfo.version,
            versionInfo.codeName.orEmpty(),
            versionInfo.buildNumber
        )
    )
}

fun PlatformEnum.asPlatform(): Platform {
    return Platform.values().first { this.name == it.name }
}

fun oshi.software.os.OperatingSystem.ProcessSort.asProcessSort(): ProcessSort {
    return ProcessSort.values().first { this.name == it.name }
}

fun ProcessSort.asOshiProcessSort(): oshi.software.os.OperatingSystem.ProcessSort {
    return oshi.software.os.OperatingSystem.ProcessSort.values().first() { this.name == it.name }
}

fun OffsetDateTime.asString() = format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)