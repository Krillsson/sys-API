package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorInput
import java.util.*

class WebServerUpMonitor(
        override val id: UUID,
        override val config: MonitorConfig<MonitoredValue.ConditionalValue>
) : Monitor<MonitoredValue.ConditionalValue>() {
    override val type: Type = Type.WEBSITE_UP

    companion object {
        val selector: ContainerConditionalValueSelector = { containers, containerMetrics, monitoredItemId ->
            containers.filter {
                it.id.equals(monitoredItemId, ignoreCase = true)
            }.map {
                if (it.state == State.RUNNING) {
                    true.toConditionalValue()
                } else {
                    false.toConditionalValue()
                }
            }.firstOrNull()
        }
    }

    override fun selectValue(event: MonitorInput): MonitoredValue.ConditionalValue? {
        return selector(event.containers, event.containerStats, config.monitoredItemId)
    }

    override fun maxValue(info: SystemInfo): MonitoredValue.ConditionalValue? {
        return MonitoredValue.ConditionalValue(true)
    }

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}