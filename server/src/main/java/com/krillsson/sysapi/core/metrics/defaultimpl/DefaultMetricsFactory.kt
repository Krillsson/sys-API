package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
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
        ticker: Ticker,
        utils: Utils,
        measurementManager: SpeedMeasurementManager
    ): DefaultMetrics {
        val defaultCpuSensors = DefaultCpuSensors(hal)
        val cpuMetrics = DefaultCpuMetrics(hal, os, defaultCpuSensors, utils, ticker)
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
        return DefaultMetrics(
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