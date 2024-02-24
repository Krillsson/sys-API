package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object RaspbianMetricsFactory {
    fun create(
        configuration: SysAPIConfiguration,
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: PlatformEnum,
        periodicTaskManager: TaskManager,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager,
    ): RaspbianMetrics {
        val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor, periodicTaskManager)
        val raspbianCpuSensors = RaspbianCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, raspbianCpuSensors, defaultCpuLoadMetrics)
        val networkMetrics = DefaultNetworkMetrics(
            hal,
            networkUploadDownloadRateMeasurementManager,
            connectivityCheckManager
        )
        val gpuMetrics = DefaultGpuMetrics(hal)
        val driveMetrics = DefaultDriveMetrics(os, hal)
        val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
        val fileSystemMetrics = DefaultFileSystemMetrics(os)
        val processesMetrics = DefaultProcessesMetrics(configuration.processes, os, hal, periodicTaskManager)
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
        return RaspbianMetrics(
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