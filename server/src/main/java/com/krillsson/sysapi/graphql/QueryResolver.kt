package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryEntry
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.util.EnvironmentUtils
import oshi.software.os.OperatingSystem

class QueryResolver : GraphQLQueryResolver {

    var metrics: Metrics? = null
    var monitorManager: MonitorManager? = null
    var historyManager: HistoryManager? = null
    var os: OperatingSystem? = null

    fun system(): SystemInfo {
        return SystemInfo(
                EnvironmentUtils.getHostName(),
                os,
                oshi.SystemInfo.getCurrentPlatformEnum(),
                metrics?.cpuMetrics()?.cpuInfo(),
                metrics?.motherboardMetrics()?.motherboard(),
                metrics?.memoryMetrics()?.memoryLoad(),
                metrics?.driveMetrics()?.drives(),
                metrics?.networkMetrics()?.networkInterfaces(),
                metrics?.gpuMetrics()?.gpus()
        )
    }

    fun history() : List<HistoryEntry<SystemLoad>> {
        return historyManager?.history?.toList().orEmpty()
    }

    fun monitors() : List<Monitor> {
        return monitorManager?.monitors()?.toList().orEmpty()
    }

    fun events() = monitorManager?.events()?.toList().orEmpty()


}