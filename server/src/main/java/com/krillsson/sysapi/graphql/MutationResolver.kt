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

    lateinit var metrics: Metrics
    lateinit var monitorManager: MonitorManager
    lateinit var eventManager: EventManager
    lateinit var historyManager: HistoryManager

    fun initialize(metrics: Metrics, monitorManager: MonitorManager, eventManager: EventManager, historyManager: HistoryManager) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.historyManager = historyManager
    }

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