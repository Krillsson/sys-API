package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class DiskWriteRateMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.DISK_WRITE_RATE

    companion object {
        val selector: NumericalValueSelector = { load, monitoredItemId ->
            load.diskLoads.firstOrNull { i: DiskLoad ->
                i.serial.equals(monitoredItemId, ignoreCase = true) || i.name.equals(
                    monitoredItemId,
                    ignoreCase = true
                )
            }?.speed?.writeBytesPerSecond?.toNumericalValue()
        }
    }

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.NumericalValue? =
        selector(event.load, config.monitoredItemId)

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value > config.threshold
    }
}