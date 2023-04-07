package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.util.PeriodicTaskManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object RaspbianMetricsFactory {
    fun create(
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: PlatformEnum,
        periodicTaskManager: PeriodicTaskManager,
        measurementManager: SpeedMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager,
    ): RaspbianMetrics {
        val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor, periodicTaskManager)
        val raspbianCpuSensors = RaspbianCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, raspbianCpuSensors, defaultCpuLoadMetrics)
        val networkMetrics = DefaultNetworkMetrics(periodicTaskManager, hal, measurementManager, connectivityCheckManager)
        val gpuMetrics = DefaultGpuMetrics(hal)
        val driveMetrics = DefaultDriveMetrics(os, hal, measurementManager)
        val diskMetrics = DefaultDiskMetrics(hal, measurementManager)
        val fileSystemMetrics = DefaultFileSystemMetrics(os)
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