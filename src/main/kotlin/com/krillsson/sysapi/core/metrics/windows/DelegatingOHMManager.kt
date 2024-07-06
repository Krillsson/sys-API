package com.krillsson.sysapi.core.metrics.windows

import ohmwrapper.*

class DelegatingOHMManager(private val monitorManager: MonitorManager) {
    fun OHMMonitors(): Array<OHMMonitor> {
        return monitorManager.OHMMonitors()
    }

    fun gpuMonitors(): Array<GpuMonitor> {
        return monitorManager.GpuMonitors()
    }

    fun cpuMonitors(): Array<CpuMonitor> {
        return monitorManager.CpuMonitors()
    }

    fun driveMonitors(): Array<DriveMonitor> {
        return monitorManager.DriveMonitors()
    }

    val mainboardMonitor: MainboardMonitor
        get() = monitorManager.mainboardMonitor

    val ramMonitor: RamMonitor
        get() = monitorManager.ramMonitor

    val networkMonitor: NetworkMonitor
        get() = monitorManager.networkMonitor

    fun update() {
        monitorManager.Update()
    }
}

