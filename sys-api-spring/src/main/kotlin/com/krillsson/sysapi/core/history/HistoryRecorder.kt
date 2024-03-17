package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.metrics.Metrics
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

class HistoryRecorder(
    private val configuration: HistoryConfiguration,
    private val metrics: Metrics,
    private val history: HistoryRepository
) {

    /*
*
* often: 5s
lessOften: 15s
seldom:5min
verySeldom:30min
*
* */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun run() {
        val metrics = metrics.systemMetrics().systemLoad(ProcessSort.MEMORY, -1)
        history.record(metrics.asHistorySystemLoad())
        history.purge(configuration.purging.olderThan, configuration.purging.unit)
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