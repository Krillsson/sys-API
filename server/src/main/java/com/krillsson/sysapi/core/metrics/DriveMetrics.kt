package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import java.util.*

interface DriveMetrics {
    fun drives(): List<Drive>
    fun driveLoads(): List<DriveLoad>
    fun driveByName(name: String): Optional<Drive>
    fun driveLoadByName(name: String): Optional<DriveLoad>
}