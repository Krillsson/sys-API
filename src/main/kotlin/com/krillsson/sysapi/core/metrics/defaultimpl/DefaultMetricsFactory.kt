package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckService
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.util.asOperatingSystem
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

@Component
class DefaultMetricsFactory {
    fun create(
            config: YAMLConfigFile,
            os: OperatingSystem,
            hal: HardwareAbstractionLayer,
            platformEnum: Platform,
            diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
            networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
            connectivityCheckService: ConnectivityCheckService
    ): DefaultMetrics {
        val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor)
        val defaultCpuSensors = DefaultCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, defaultCpuSensors, defaultCpuLoadMetrics)
        val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
        val fileSystemMetrics = DefaultFileSystemMetrics(os)
        val networkMetrics = DefaultNetworkMetrics(
            hal,
            networkUploadDownloadRateMeasurementManager,
            connectivityCheckService
        )
        val gpuMetrics = DefaultGpuMetrics(hal)
        val processesMetrics = DefaultProcessesMetrics(config, os, hal)
        val motherboardMetrics = DefaultMotherboardMetrics(hal)
        val memoryMetrics = DefaultMemoryMetrics(hal, os)
        val systemMetrics = DefaultSystemMetrics(
            cpuMetrics,
            networkMetrics,
            diskMetrics,
            fileSystemMetrics,
            memoryMetrics,
            processesMetrics,
            motherboardMetrics,
            gpuMetrics,
            os.asOperatingSystem(),
            platformEnum
        )
        return DefaultMetrics(
            cpuMetrics,
            networkMetrics,
            gpuMetrics,
            diskMetrics,
            fileSystemMetrics,
            processesMetrics,
            motherboardMetrics,
            memoryMetrics,
            systemMetrics
        )
    }
}