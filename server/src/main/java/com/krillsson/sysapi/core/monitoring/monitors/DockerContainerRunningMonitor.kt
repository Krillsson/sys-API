package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class DockerContainerRunningMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: MonitorType = MonitorType.CONTAINER_RUNNING

    companion object {
        const val RUNNING = 1.0
        const val STOPPED = 0.0
    }

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        return event.containers.filter {
            it.id.equals(config.monitoredItemId, ignoreCase = true)
        }.map {
            if (it.state == State.RUNNING) {
                RUNNING
            } else {
                STOPPED
            }
        }.firstOrNull() ?: STOPPED
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != RUNNING
    }
}