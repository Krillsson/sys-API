package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.MetricQueryEvent
import com.krillsson.sysapi.core.monitoring.Monitor
import java.util.*

class DriveReadRateMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.DRIVE_READ_RATE

    companion object {
        val selector: NumericalValueSelector = { load, monitoredItemId ->
            load.driveLoads.firstOrNull { i: DriveLoad ->
                i.serial.equals(monitoredItemId, ignoreCase = true) || i.name.equals(
                    monitoredItemId,
                    ignoreCase = true
                )
            }?.speed?.readBytesPerSecond?.toNumericalValue()
        }
    }

    override fun selectValue(event: MetricQueryEvent): MonitoredValue.NumericalValue? = selector(event.load, config.monitoredItemId)

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value > config.threshold
    }
}