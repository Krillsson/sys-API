package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object DefaultMetricsFactory {
    fun create(
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: PlatformEnum,
        periodicTaskManager: TaskManager,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager
    ): DefaultMetrics {
        val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor, periodicTaskManager)
        val defaultCpuSensors = DefaultCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, defaultCpuSensors, defaultCpuLoadMetrics)
        val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
        val fileSystemMetrics = DefaultFileSystemMetrics(os)
        val networkMetrics = DefaultNetworkMetrics(
            hal,
            networkUploadDownloadRateMeasurementManager,
            connectivityCheckManager
        )
        val gpuMetrics = DefaultGpuMetrics(hal)
        val driveMetrics = DefaultDriveMetrics(os, hal)
        val processesMetrics = DefaultProcessesMetrics(os, hal, periodicTaskManager)
        val motherboardMetrics = DefaultMotherboardMetrics(hal)
        val memoryMetrics = DefaultMemoryMetrics(hal, os)
        val systemMetrics = DefaultSystemMetrics(
            cpuMetrics,
            networkMetrics,
            driveMetrics,
            diskMetrics,
            fileSystemMetrics,
            memoryMetrics,
            processesMetrics,
            motherboardMetrics,
            gpuMetrics,
            os.asOperatingSystem(),
            platformEnum.asPlatform()
        )
        return DefaultMetrics(
            cpuMetrics,
            networkMetrics,
            gpuMetrics,
            driveMetrics,
            diskMetrics,
            fileSystemMetrics,
            processesMetrics,
            motherboardMetrics,
            memoryMetrics,
            systemMetrics
        )
    }
}