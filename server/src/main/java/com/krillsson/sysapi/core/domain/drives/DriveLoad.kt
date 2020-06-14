package com.krillsson.sysapi.core.domain.drives

class DriveLoad(
    val name: String,
    val serial: String,
    val values: DriveValues,
    val speed: DriveSpeed,
    val health: DriveHealth
) 