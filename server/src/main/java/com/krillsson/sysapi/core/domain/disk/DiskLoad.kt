package com.krillsson.sysapi.core.domain.disk

import com.krillsson.sysapi.core.domain.drives.DriveHealth
import com.krillsson.sysapi.core.domain.drives.DriveSpeed

data class DiskLoad(
    val name: String,
    val serial: String,
    val values: DiskValues,
    val speed: DriveSpeed,
    val health: DriveHealth
)