package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class DriveMonitor(override val id: UUID, override val config: Config) : Monitor() {
    override val type: MonitorType = MonitorType.DRIVE_SPACE

    override fun selectValue(load: SystemLoad): Double = load.driveLoads
            .stream()
            .filter { i: DriveLoad -> i.serial.equals(config.id, ignoreCase = true) || i.name.equals(config.id, ignoreCase = true) }
            .findFirst()
            .orElse(Empty.DRIVE_LOAD)
            .values
            .usableSpace.toDouble()

    override fun isPastThreshold(value: Double): Boolean {
        return value < config.threshold
    }
}