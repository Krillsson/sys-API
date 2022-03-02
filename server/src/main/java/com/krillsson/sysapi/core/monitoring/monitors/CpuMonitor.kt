package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class CpuMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.FractionalValue>) :
    Monitor<MonitoredValue.FractionalValue>() {

    companion object {
        val selector: FractionalValueSelector = { load, _ ->
            MonitoredValue.FractionalValue(load.cpuLoad.usagePercentage.toFloat())
        }
    }

    override val type: Type = Type.CPU_LOAD

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.FractionalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.FractionalValue): Boolean {
        return value > config.threshold
    }
}