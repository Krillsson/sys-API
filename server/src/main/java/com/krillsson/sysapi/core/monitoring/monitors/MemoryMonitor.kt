package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class MemoryMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) : Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.MEMORY_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.NumericalValue {
        return event.load().memory.availableBytes.toNumericalValue()
    }

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value < config.threshold
    }
}