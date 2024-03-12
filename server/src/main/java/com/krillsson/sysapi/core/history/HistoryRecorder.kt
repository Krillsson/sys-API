package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskInterval
import com.krillsson.sysapi.periodictasks.TaskManager
import io.dropwizard.lifecycle.Managed

class HistoryRecorder(
    private val taskManager: TaskManager,
    private val configuration: HistoryConfiguration,
    private val metrics: Metrics,
    private val history: HistoryRepository
) : Managed, Task {

    override val defaultInterval: TaskInterval = TaskInterval.VerySeldom
    override val key: Task.Key = Task.Key.StoreMetricHistoryEntry

    override fun run() {
        val metrics = metrics.systemMetrics().systemLoad(ProcessSort.MEMORY, -1)
        history.record(metrics.asHistorySystemLoad())
        history.purge(configuration.purging.olderThan, configuration.purging.unit)
    }

    override fun start() {
        taskManager.registerTask(this)
    }

    private fun com.krillsson.sysapi.core.domain.system.SystemLoad.asHistorySystemLoad(): HistorySystemLoad {
        return HistorySystemLoad(
            uptime,
            systemLoadAverage,
            cpuLoad,
            networkInterfaceLoads,
            connectivity,
            diskLoads,
            fileSystemLoads,
            memory,
            gpuLoads,
            motherboardHealth
        )
    }
}