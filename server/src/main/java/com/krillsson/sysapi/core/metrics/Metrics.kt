package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.system.SystemLoad
import oshi.software.os.OperatingSystem.ProcessSort

interface Metrics {
    fun initialize()
    fun cpuMetrics(): CpuMetrics
    fun networkMetrics(): NetworkMetrics
    fun driveMetrics(): DriveMetrics
    fun memoryMetrics(): MemoryMetrics
    fun processesMetrics(): ProcessesMetrics
    fun gpuMetrics(): GpuMetrics
    fun motherboardMetrics(): MotherboardMetrics

    fun consolidatedMetrics(
        sort: ProcessSort = ProcessSort.MEMORY,
        limit: Int = -1
    ): SystemLoad {
        return SystemLoad(
            cpuMetrics().uptime(),
            cpuMetrics().cpuLoad().systemLoadAverage,
            cpuMetrics().cpuLoad(),
            networkMetrics().networkInterfaceLoads(),
            driveMetrics().driveLoads(),
            memoryMetrics().memoryLoad(),
            processesMetrics().processesInfo(sort, limit).processes, gpuMetrics().gpuLoads(),
            motherboardMetrics().motherboardHealth()
        )
    }
}