package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.MetricQueryEvent
import com.krillsson.sysapi.core.monitoring.Monitor
import java.util.*

class FileSystemSpaceMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.FILE_SYSTEM_SPACE

    companion object {
        val selector: NumericalValueSelector = { load, monitoredItemId ->
            load.fileSystemLoads.firstOrNull { i: FileSystemLoad ->
                i.id.equals(monitoredItemId, ignoreCase = true)
            }?.usableSpaceBytes?.toNumericalValue()
        }
    }

    override fun selectValue(event: MetricQueryEvent): MonitoredValue.NumericalValue? =
        selector(event.load, config.monitoredItemId)

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value < config.threshold
    }
}