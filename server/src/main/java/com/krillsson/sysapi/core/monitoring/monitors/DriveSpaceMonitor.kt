package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class DriveSpaceMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) : Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.DRIVE_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.NumericalValue = event.load().driveLoads
            .stream()
            .filter { i: DriveLoad -> i.serial.equals(config.monitoredItemId, ignoreCase = true) || i.name.equals(config.monitoredItemId, ignoreCase = true) }
            .findFirst()
            .orElse(Empty.DRIVE_LOAD)
            .values
            .usableSpace.toNumericalValue()

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value < config.threshold
    }
}