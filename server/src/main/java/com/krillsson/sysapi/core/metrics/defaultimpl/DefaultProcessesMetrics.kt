package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.processes.ProcessSort.*
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.util.OSProcessComparators
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.measureTimeMillis
import org.slf4j.LoggerFactory
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSProcess
import oshi.software.os.OperatingSystem
import java.util.*

class DefaultProcessesMetrics(
    private val operatingSystem: OperatingSystem,
    private val hal: HardwareAbstractionLayer,
    private val ticker: Ticker,
) : ProcessesMetrics, Ticker.TickListener {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProcessesMetrics::class.java)
    }

    private val priorSnapshotMap: MutableMap<Int, OSProcess> = mutableMapOf()
    private val currentLoad: MutableMap<Int, Double> = mutableMapOf()

    init {
        updateMap(operatingSystem.getProcesses(0, null))
    }

    override fun getProcessByPid(pid: Int): Optional<Process> {
        val process: OSProcess? = priorSnapshotMap[pid]
        return Optional.ofNullable(
            process?.asProcess(
                currentLoad.getOrDefault(pid, 0.0),
                hal.memory.total
            )
        )
    }

    private fun sortAndLimit(
        processes: MutableList<OSProcess>,
        sortBy: ProcessSort,
        limit: Int
    ): List<OSProcess> {
        when (sortBy) {
            CPU -> processes.sortWith(OSProcessComparators.CPU_LOAD_COMPARATOR)
            MEMORY -> processes.sortWith(OSProcessComparators.MEMORY_COMPARATOR)
            OLDEST -> processes.sortWith(OSProcessComparators.OLDEST_COMPARATOR)
            NEWEST -> processes.sortWith(OSProcessComparators.NEWEST_COMPARATOR)
            PID -> processes.sortWith(OSProcessComparators.PID_COMPARATOR)
            PARENTPID -> processes.sortWith(OSProcessComparators.PARENT_PID_COMPARATOR)
            NAME -> processes.sortWith(OSProcessComparators.NAME_COMPARATOR)
        }
        return if (limit <= 0) processes
        else processes.take(limit)
    }

    override fun processesInfo(sortBy: ProcessSort, limit: Int): ProcessesInfo {
        val tracedValue = measureTimeMillis {  sortAndLimit(priorSnapshotMap.values.toMutableList(), sortBy, limit) }
        LOGGER.debug(
            "Took {} to sort and limit {} processes",
            "${tracedValue.first.toInt()}ms",
            tracedValue.second.size
        )
        return ProcessesInfo(
            operatingSystem.processId.toLong(),
            operatingSystem.threadCount.toLong(),
            operatingSystem.processCount.toLong(),
            tracedValue.second.map { process ->
                process.asProcess(
                    currentLoad.getOrDefault(process.processID, 0.0),
                    hal.memory.total
                )
            }
        )
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
        val cpuCount: Int = hal.processor.logicalProcessorCount

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

    private fun OSProcess.asProcess(cpuPercent: Double, totalBytes: Long): Process {
        return Process(
            name,
            path,
            commandLine,
            user,
            userID,
            group,
            groupID,
            state,
            processID,
            parentProcessID,
            threadCount,
            priority,
            virtualSize,
            residentSetSize,
            100.0 * residentSetSize / totalBytes,
            kernelTime, userTime,
            upTime,
            cpuPercent,
            startTime,
            bytesRead,
            bytesWritten
        )
    }
}