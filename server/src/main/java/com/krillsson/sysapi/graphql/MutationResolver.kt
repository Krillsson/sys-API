package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.mutations.CreateMonitorInput
import com.krillsson.sysapi.graphql.mutations.DeleteEventInput
import com.krillsson.sysapi.graphql.mutations.DeleteMonitorInput
import oshi.software.os.OperatingSystem

class MutationResolver : GraphQLMutationResolver {

    var metrics: Metrics? = null
    var monitorManager: MonitorManager? = null
    var historyManager: HistoryManager? = null
    var os: OperatingSystem? = null

    fun createMonitor(input: CreateMonitorInput): String? {
        return monitorManager?.createAndAdd(input.inertiaInSeconds, input.type, input.threshold.toDouble())
    }

    fun deleteMonitor(input: DeleteMonitorInput): String? {
        return monitorManager?.remove(input.id)
    }

    fun deleteEvent(input: DeleteEventInput): String? {
        return monitorManager?.removeEvents(input.id)
    }
}