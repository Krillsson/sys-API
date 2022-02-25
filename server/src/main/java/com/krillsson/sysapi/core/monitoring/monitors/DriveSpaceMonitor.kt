package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class DriveSpaceMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: MonitorType = MonitorType.DRIVE_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): Double = event.load().driveLoads
            .stream()
            .filter { i: DriveLoad -> i.serial.equals(config.monitoredItemId, ignoreCase = true) || i.name.equals(config.monitoredItemId, ignoreCase = true) }
            .findFirst()
            .orElse(Empty.DRIVE_LOAD)
            .values
            .usableSpace.toDouble()

    override fun isPastThreshold(value: Double): Boolean {
        return value < config.threshold
    }
}