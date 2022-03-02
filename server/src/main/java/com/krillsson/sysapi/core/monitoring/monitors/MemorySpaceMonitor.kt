package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class MemorySpaceMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) :
    Monitor<MonitoredValue.NumericalValue>() {
    companion object {
        val selector: NumericalValueSelector = { load, _ ->
            load.memory.availableBytes.toNumericalValue()
        }
    }

    override val type: Type = Type.MEMORY_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.NumericalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value < config.threshold
    }
}