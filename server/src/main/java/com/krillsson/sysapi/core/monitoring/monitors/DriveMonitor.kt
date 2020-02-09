package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.monitoring.MonitorInput
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.Duration
import java.util.*

class DriveMonitor(override val id: UUID, private val driveId: String, override val inertia: Duration, override val threshold: Double) : MonitorInput {
    override val type: MonitorType = MonitorType.DRIVE_SPACE

    override fun value(systemLoad: SystemLoad): Double {
        return systemLoad.driveLoads
                .stream()
                .filter { i: DriveLoad -> i.serial.equals(driveId, ignoreCase = true) }
                .findFirst().orElse(Empty.DRIVE_LOAD)
                .values
                .usableSpace.toDouble()
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value < threshold
    }
}