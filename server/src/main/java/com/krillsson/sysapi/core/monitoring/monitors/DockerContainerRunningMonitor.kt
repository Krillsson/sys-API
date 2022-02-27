package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class DockerContainerRunningMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.BooleanValue>) : Monitor<MonitoredValue.BooleanValue>() {
    override val type: Type = Type.CONTAINER_RUNNING

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.BooleanValue {
        return event.containers.filter {
            it.id.equals(config.monitoredItemId, ignoreCase = true)
        }.map {
            if (it.state == State.RUNNING) {
                true.toBooleanValue()
            } else {
                false.toBooleanValue()
            }
        }.firstOrNull() ?: false.toBooleanValue()
    }

    override fun isPastThreshold(value: MonitoredValue.BooleanValue): Boolean {
        return !value.value
    }
}