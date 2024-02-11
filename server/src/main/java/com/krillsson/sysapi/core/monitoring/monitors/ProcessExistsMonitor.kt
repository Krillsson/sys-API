package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.monitoring.MetricQueryEvent
import com.krillsson.sysapi.core.monitoring.Monitor
import java.util.*

class ProcessExistsMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.ConditionalValue>
) : Monitor<MonitoredValue.ConditionalValue>() {

    companion object {
        val selector: ConditionalValueSelector = { load, monitoredItemID ->
            val pid = monitoredItemID?.toInt()
            load.processes.any { it.processID == pid }.toConditionalValue()
        }
    }

    override val type: Type = Type.PROCESS_EXISTS

    override fun selectValue(event: MetricQueryEvent): MonitoredValue.ConditionalValue? =
        selector(event.load, config.monitoredItemId)

    override fun maxValue(info: SystemInfo): MonitoredValue.ConditionalValue {
        return MonitoredValue.ConditionalValue(true)
    }

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}