package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object RaspbianMetricsFactory {
    fun create(
        configuration: YAMLConfigFile,
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: Platform,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager,
    ): RaspbianMetrics {
        val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor)
        val raspbianCpuSensors = RaspbianCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, raspbianCpuSensors, defaultCpuLoadMetrics)
        val networkMetrics = DefaultNetworkMetrics(
            hal,
            networkUploadDownloadRateMeasurementManager,
            connectivityCheckManager
        )
        val gpuMetrics = DefaultGpuMetrics(hal)
        val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
        val fileSystemMetrics = DefaultFileSystemMetrics(os)
        val processesMetrics = DefaultProcessesMetrics(configuration, os, hal)
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
        return RaspbianMetrics(
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