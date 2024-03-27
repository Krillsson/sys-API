package com.krillsson.sysapi.core.domain.system

class OperatingSystem(
    val manufacturer: String,
    val family: String,
    val versionInfo: VersionInfo
) {

    data class VersionInfo(
        val version: String,
        val codeName: String,
        val buildNumber: String
    )
}