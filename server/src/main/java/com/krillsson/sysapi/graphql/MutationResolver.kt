package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.mutations.*
import oshi.software.os.OperatingSystem
import java.time.Duration

class MutationResolver : GraphQLMutationResolver {

    var metrics: Metrics? = null
    var monitorManager: MonitorManager? = null
    var eventManager: EventManager? = null
    var historyManager: HistoryManager? = null
    var os: OperatingSystem? = null

    fun createMonitor(input: CreateMonitorInput): CreateMonitorOutput? {
        val createdId = monitorManager?.add(Duration.ofSeconds(input.inertiaInSeconds.toLong()), input.type, input.threshold.toDouble(), input.id)
        if (createdId != null) {
            return CreateMonitorOutput(createdId)
        }
        throw IllegalArgumentException("No matching device found for $input")
    }

    fun deleteMonitor(input: DeleteMonitorInput): DeleteMonitorOutput? {
        val removed = monitorManager?.remove(input.id) ?: false
        return DeleteMonitorOutput(removed)
    }

    fun deleteEvent(input: DeleteEventInput): DeleteEventOutput? {
        val removed = eventManager?.remove(input.id) ?: false
        return DeleteEventOutput(removed)
    }

    fun deleteEventsForMonitor(input: DeleteMonitorInput): DeleteEventOutput? {
        val removed = eventManager?.removeEventsForMonitorId(input.id) ?: false
        return DeleteEventOutput(removed)
    }
}