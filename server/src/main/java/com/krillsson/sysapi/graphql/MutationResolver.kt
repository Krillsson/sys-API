package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.mutations.*
import oshi.software.os.OperatingSystem
import java.util.*

class MutationResolver : GraphQLMutationResolver {

    var metrics: Metrics? = null
    var monitorManager: MonitorManager? = null
    var historyManager: HistoryManager? = null
    var os: OperatingSystem? = null

    fun createMonitor(input: CreateMonitorInput): CreateMonitorOutput? {
        return CreateMonitorOutput(UUID.fromString(monitorManager?.createAndAdd(input.inertiaInSeconds, input.type, input.threshold.toDouble())))
    }

    fun deleteMonitor(input: DeleteMonitorInput): DeleteMonitorOutput? {
        return DeleteMonitorOutput(UUID.fromString(monitorManager?.remove(input.id)))
    }

    fun deleteEvent(input: DeleteEventInput): DeleteEventOutput? {
        return DeleteEventOutput(UUID.fromString(monitorManager?.removeEvents(input.id)))
    }
}