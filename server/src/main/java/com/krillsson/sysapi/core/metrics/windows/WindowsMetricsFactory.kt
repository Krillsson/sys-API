package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object WindowsMetricsFactory {
    fun create(
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: PlatformEnum,
        taskManager: TaskManager,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager
    ): WindowsMetrics? {
        val ohmFactory = OHMManagerFactory()
        if (ohmFactory.prerequisitesFilled() && ohmFactory.initialize()) {

            val monitorManager = ohmFactory.monitorManager
            val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor, taskManager)

            val cpuMetrics =
                WindowsCpuMetrics(hal, os, defaultCpuLoadMetrics, monitorManager)
            val networkMetrics =
                WindowsNetworkMetrics(taskManager, hal, networkUploadDownloadRateMeasurementManager, connectivityCheckManager, monitorManager)
            val gpuMetrics = WindowsGpuMetrics(hal, monitorManager)
            val driveMetrics =
                WindowsDriveMetrics(os, hal)
            val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
            val fileSystemMetrics = DefaultFileSystemMetrics(os)
            val processesMetrics = DefaultProcessesMetrics(os, hal, taskManager)
            val motherboardMetrics = WindowsMotherboardMetrics(hal, monitorManager)
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
            return WindowsMetrics(
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

        return null
    }
}