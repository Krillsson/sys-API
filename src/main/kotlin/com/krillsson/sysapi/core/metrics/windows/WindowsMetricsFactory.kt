package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuLoadMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDiskMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultFileSystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMemoryMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultSystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object WindowsMetricsFactory {
    fun create(
        configuration: YAMLConfigFile,
        os: OperatingSystem,
        hal: HardwareAbstractionLayer,
        platformEnum: Platform,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager
    ): WindowsMetrics? {
        val ohmFactory = OHMManagerFactory()
        if (ohmFactory.prerequisitesFilled() && ohmFactory.initialize()) {

            val monitorManager = ohmFactory.monitorManager
            val defaultCpuLoadMetrics = DefaultCpuLoadMetrics(hal.processor)
            val cpuMetrics = WindowsCpuMetrics(hal, os, defaultCpuLoadMetrics, monitorManager)
            val networkMetrics = DefaultNetworkMetrics(
                hal,
                networkUploadDownloadRateMeasurementManager,
                connectivityCheckManager
            )
            val gpuMetrics = WindowsGpuMetrics(hal, monitorManager)
            val diskMetrics = DefaultDiskMetrics(hal, diskReadWriteRateMeasurementManager)
            val fileSystemMetrics = DefaultFileSystemMetrics(os)
            val processesMetrics = DefaultProcessesMetrics(configuration, os, hal)
            val motherboardMetrics = WindowsMotherboardMetrics(hal, monitorManager)
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
            return WindowsMetrics(
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

        return null
    }
}