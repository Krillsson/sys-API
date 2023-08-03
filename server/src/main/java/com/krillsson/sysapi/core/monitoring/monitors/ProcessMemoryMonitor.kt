package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.MetricQueryEvent
import com.krillsson.sysapi.core.monitoring.Monitor
import java.util.*

class ProcessMemoryMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    companion object {
        val selector: NumericalValueSelector = { load, monitoredItemID ->
            val pid = monitoredItemID?.toInt()
            load.processes.firstOrNull { it.processID == pid }?.residentSetSize?.toNumericalValue()
        }
    }

    override val type: Type = Type.PROCESS_MEMORY_SPACE

    override fun selectValue(event: MetricQueryEvent): MonitoredValue.NumericalValue? =
        selector(event.load, config.monitoredItemId)

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value > config.threshold
    }
}