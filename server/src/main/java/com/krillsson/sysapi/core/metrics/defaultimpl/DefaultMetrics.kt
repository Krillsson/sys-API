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
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem,
    private val speedMeasurementManager: SpeedMeasurementManager,
    private val ticker: Ticker,
    private val utils: Utils
) : Metrics {
    private var cpuMetrics: CpuMetrics? = null
    private var networkMetrics: NetworkMetrics? = null
    private var gpuMetrics: GpuMetrics? = null
    private var driveMetrics: DriveMetrics? = null
    private var processesMetrics: ProcessesMetrics? = null
    private var motherboardMetrics: MotherboardMetrics? = null
    private var memoryMetrics: MemoryMetrics? = null
    private var systemMetrics: SystemMetrics? = null

    override fun initialize() {
        val cpuMetrics =
            DefaultCpuMetrics(hal, operatingSystem, DefaultCpuSensors(hal), utils, ticker)
        cpuMetrics.register()
        setCpuMetrics(cpuMetrics)
        val networkInfoProvider = DefaultNetworkMetrics(hal, speedMeasurementManager)
        networkInfoProvider.register()
        setNetworkMetrics(networkInfoProvider)
        setGpuMetrics(DefaultGpuMetrics(hal))
        val diskInfoProvider =
            DefaultDriveMetrics(
                operatingSystem,
                hal,
                speedMeasurementManager
            )
        diskInfoProvider.register()
        setDriveMetrics(diskInfoProvider)
        setProcessesMetrics(DefaultProcessesMetrics(operatingSystem, hal))
        setMotherboardMetrics(DefaultMotherboardMetrics(hal))
        setMemoryMetrics(DefaultMemoryMetrics(hal, operatingSystem))
    }

    override fun cpuMetrics(): CpuMetrics {
        return cpuMetrics!!
    }

    override fun networkMetrics(): NetworkMetrics {
        return networkMetrics!!
    }

    override fun driveMetrics(): DriveMetrics {
        return driveMetrics!!
    }

    override fun memoryMetrics(): MemoryMetrics {
        return memoryMetrics!!
    }

    override fun processesMetrics(): ProcessesMetrics {
        return processesMetrics!!
    }

    override fun gpuMetrics(): GpuMetrics {
        return gpuMetrics!!
    }

    override fun motherboardMetrics(): MotherboardMetrics {
        return motherboardMetrics!!
    }

    override fun systemMetrics(): SystemMetrics {
        return systemMetrics!!
    }

    protected fun setCpuMetrics(cpuMetrics: CpuMetrics?) {
        this.cpuMetrics = cpuMetrics
    }

    protected fun setNetworkMetrics(networkMetrics: NetworkMetrics?) {
        this.networkMetrics = networkMetrics
    }

    protected fun setGpuMetrics(gpuMetrics: GpuMetrics?) {
        this.gpuMetrics = gpuMetrics
    }

    protected fun setDriveMetrics(driveMetrics: DriveMetrics?) {
        this.driveMetrics = driveMetrics
    }

    protected fun setProcessesMetrics(processesMetrics: ProcessesMetrics?) {
        this.processesMetrics = processesMetrics
    }

    protected fun setMotherboardMetrics(motherboardMetrics: MotherboardMetrics?) {
        this.motherboardMetrics = motherboardMetrics
    }

    protected fun setMemoryMetrics(memoryMetrics: MemoryMetrics?) {
        this.memoryMetrics = memoryMetrics
    }

    protected fun setSystemMetrics(systemMetrics: SystemMetrics?) {
        this.systemMetrics = systemMetrics
    }
}