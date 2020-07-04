package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.GpuMetrics
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.util.EnvironmentUtils

open class DefaultSystemMetrics(
    private val cpuMetrics: CpuMetrics,
    private val networkMetrics: NetworkMetrics,
    private val driveMetrics: DriveMetrics,
    private val memoryMetrics: MemoryMetrics,
    private val processesMetrics: ProcessesMetrics,
    private val motherboardMetrics: MotherboardMetrics,
    private val gpuMetrics: GpuMetrics,
    private val operatingSystem: OperatingSystem,
    private val platform: Platform
) : SystemMetrics {

    override fun systemLoad(sort: ProcessSort, limit: Int): SystemLoad {
        return SystemLoad(
            cpuMetrics.uptime(),
            cpuMetrics.cpuLoad().systemLoadAverage,
            cpuMetrics.cpuLoad(),
            networkMetrics.networkInterfaceLoads(),
            driveMetrics.driveLoads(),
            memoryMetrics.memoryLoad(),
            processesMetrics.processesInfo(sort, limit).processes,
            gpuMetrics.gpuLoads(),
            motherboardMetrics.motherboardHealth()
        )
    }

    override fun systemInfo(): SystemInfo {
        return SystemInfo(
            EnvironmentUtils.getHostName(),
            operatingSystem,
            platform,
            cpuMetrics.cpuInfo(),
            motherboardMetrics.motherboard(),
            memoryMetrics.memoryLoad(),
            driveMetrics.drives(),
            networkMetrics.networkInterfaces(),
            gpuMetrics.gpus()
        )
    }
}