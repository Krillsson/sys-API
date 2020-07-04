package com.krillsson.sysapi.util

import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import oshi.PlatformEnum

fun oshi.software.os.OperatingSystem.asOperatingSystem(): OperatingSystem {
    return OperatingSystem(
        manufacturer,
        family,
        OperatingSystem.VersionInfo(
            versionInfo.version,
            versionInfo.codeName,
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