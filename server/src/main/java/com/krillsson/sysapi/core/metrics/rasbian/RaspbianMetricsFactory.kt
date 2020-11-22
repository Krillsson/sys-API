package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultGpuMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMemoryMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMotherboardMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultSystemMetrics
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
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
        ticker: Ticker,
        utils: Utils,
        measurementManager: SpeedMeasurementManager
    ): RaspbianMetrics {
        val raspbianCpuSensors = RaspbianCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, raspbianCpuSensors, utils, ticker)
        val networkMetrics = DefaultNetworkMetrics(hal, measurementManager)
        val gpuMetrics = DefaultGpuMetrics(hal)
        val driveMetrics = DefaultDriveMetrics(os, hal, measurementManager)
        val processesMetrics = DefaultProcessesMetrics(os, hal)
        val motherboardMetrics = DefaultMotherboardMetrics(hal)
        val memoryMetrics = DefaultMemoryMetrics(hal, os)
        val systemMetrics = DefaultSystemMetrics(
            cpuMetrics,
            networkMetrics,
            driveMetrics,
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
            processesMetrics,
            motherboardMetrics,
            memoryMetrics,
            systemMetrics
        )
    }
}