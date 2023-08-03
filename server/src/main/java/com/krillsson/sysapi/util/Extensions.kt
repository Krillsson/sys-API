package com.krillsson.sysapi.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import oshi.PlatformEnum
import oshi.software.os.OSProcess
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
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

fun Instant.toOffsetDateTime() = atZone(ZoneId.systemDefault()).toOffsetDateTime()

fun PlatformEnum.asPlatform(): Platform {
    return Platform.values().first { this.name == it.name }
}

fun ProcessSort.asOshiProcessSort(): Comparator<OSProcess> {
    return when (this) {
        ProcessSort.CPU -> oshi.software.os.OperatingSystem.ProcessSorting.CPU_DESC
        ProcessSort.MEMORY -> oshi.software.os.OperatingSystem.ProcessSorting.RSS_DESC
        ProcessSort.OLDEST -> oshi.software.os.OperatingSystem.ProcessSorting.UPTIME_DESC
        ProcessSort.NEWEST -> oshi.software.os.OperatingSystem.ProcessSorting.UPTIME_ASC
        ProcessSort.PID -> oshi.software.os.OperatingSystem.ProcessSorting.PID_ASC
        ProcessSort.PARENTPID -> oshi.software.os.OperatingSystem.ProcessSorting.PARENTPID_ASC
        ProcessSort.NAME -> oshi.software.os.OperatingSystem.ProcessSorting.NAME_ASC
    }
}

fun OffsetDateTime.asString() = format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this::class.java.name.removeSuffix("\$Companion")) }
}

fun Any.reflectionToString(): String {
    return ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .writerWithDefaultPrettyPrinter().writeValueAsString(this)
}

fun Int.asHex() = Integer.toHexString(this).uppercase()
