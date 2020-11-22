package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.util.Ticker
import oshi.hardware.CentralProcessor
import oshi.software.os.OSProcess
import oshi.software.os.OperatingSystem

class DefaultProcessesCpuLoadMetrics(
    private val processor: CentralProcessor,
    private val operatingSystem: OperatingSystem,
    private val ticker: Ticker
) : Ticker.TickListener {

    private val priorSnapshotMap: MutableMap<Int, OSProcess> = mutableMapOf()
    private val currentLoad: MutableMap<Int, Double> = mutableMapOf()

    init {
        updateMap(operatingSystem.getProcesses(0, null))
    }

    fun register() {
        ticker.register(this)
    }

    override fun onTick() {
        val processes = operatingSystem.getProcesses(0, null)
        updateCurrentLoad(processes)
        updateMap(processes)
    }

    private fun updateCurrentLoad(processes: List<OSProcess>) {
        val cpuCount: Int = processor.logicalProcessorCount

        for (process in processes) {
            currentLoad[process.processID] =
                100.0 * process.getProcessCpuLoadBetweenTicks(priorSnapshotMap[process.processID]) / cpuCount
        }
    }

    private fun updateMap(processes: List<OSProcess>) {
        priorSnapshotMap.clear()
        for (process in processes) {
            priorSnapshotMap[process.processID] = process
        }
    }
}