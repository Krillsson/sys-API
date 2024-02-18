package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorInput
import java.util.*

class DriveWriteRateMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.DRIVE_WRITE_RATE

    companion object {
        val selector: NumericalValueSelector = { load, monitoredItemId ->
            load.driveLoads.firstOrNull { i: DriveLoad ->
                i.serial.equals(monitoredItemId, ignoreCase = true) || i.name.equals(
                    monitoredItemId,
                    ignoreCase = true
                )
            }?.speed?.writeBytesPerSecond?.toNumericalValue()
        }
    }

    override fun selectValue(event: MonitorInput): MonitoredValue.NumericalValue? =
        selector(event.load, config.monitoredItemId)

    override fun maxValue(info: SystemInfo): MonitoredValue.NumericalValue? {
        // have no way of knowing this
        return null
    }

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value > config.threshold
    }
}