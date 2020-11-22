package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.GpuMetrics
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

open class DefaultMetrics(
    private val cpuMetrics: DefaultCpuMetrics,
    private val networkMetrics: DefaultNetworkMetrics,
    private val gpuMetrics: GpuMetrics,
    private val driveMetrics: DefaultDriveMetrics,
    private val processesMetrics: ProcessesMetrics,
    private val motherboardMetrics: MotherboardMetrics,
    private val memoryMetrics: MemoryMetrics,
    private val systemMetrics: SystemMetrics,
) : Metrics {

    override fun initialize() {
        cpuMetrics.register()
        driveMetrics.register()
        networkMetrics.register()
    }

    override fun cpuMetrics(): CpuMetrics {
        return cpuMetrics
    }

    override fun networkMetrics(): NetworkMetrics {
        return networkMetrics
    }

    override fun driveMetrics(): DriveMetrics {
        return driveMetrics
    }

    override fun memoryMetrics(): MemoryMetrics {
        return memoryMetrics
    }

    override fun processesMetrics(): ProcessesMetrics {
        return processesMetrics
    }

    override fun gpuMetrics(): GpuMetrics {
        return gpuMetrics
    }

    override fun motherboardMetrics(): MotherboardMetrics {
        return motherboardMetrics
    }

    override fun systemMetrics(): SystemMetrics {
        return systemMetrics
    }
}