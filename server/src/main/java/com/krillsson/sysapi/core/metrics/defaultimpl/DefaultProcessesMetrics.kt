package com.krillsson.sysapi.core.metrics.defaultimpl

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.krillsson.sysapi.config.ProcessesConfiguration
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.processes.ProcessSort.*
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskInterval
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.OSProcessComparators
import com.krillsson.sysapi.util.measureTimeMillis
import org.slf4j.LoggerFactory
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSProcess
import oshi.software.os.OperatingSystem
import java.time.Duration
import java.util.*

class DefaultProcessesMetrics(
    private val config: ProcessesConfiguration,
    private val operatingSystem: OperatingSystem,
    private val hal: HardwareAbstractionLayer,
    private val taskManager: TaskManager,
) : ProcessesMetrics, Task {

    override val defaultInterval: TaskInterval = TaskInterval.LessOften
    override val key: Task.Key = Task.Key.UpdateProcessesList

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProcessesMetrics::class.java)
    }

    private val priorSnapshotMap: MutableMap<Int, OSProcess> = mutableMapOf()
    private val currentLoad: MutableMap<Int, Double> = mutableMapOf()

    // profiling showed that user and group was expensive in Linux
    private val cachedUserAndGroup: Cache<Int, Pair<String, String>> = CacheBuilder.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(5))
        .build()

    init {
        doIfEnabled {
            updateMap(operatingSystem.processes)
        }
    }

    fun register() {
        taskManager.registerTask(this)
    }

    override fun run() {
        doIfEnabled {
            val processes = operatingSystem.processes
            updateCurrentLoad(processes)
            updateMap(processes)
        }
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
        val tracedValue = measureTimeMillis { sortAndLimit(priorSnapshotMap.values.toMutableList(), sortBy, limit) }
        LOGGER.trace(
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

    private fun OSProcess.getOrCreateUserAndGroup(pid: Int): Pair<String, String> {
        return cachedUserAndGroup.get(pid) { user to group }
    }

    private fun OSProcess.asProcess(cpuPercent: Double, totalBytes: Long): Process {
        val (user, group) = getOrCreateUserAndGroup(processID)
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
            kernelTime,
            userTime,
            upTime,
            cpuPercent,
            startTime,
            bytesRead,
            bytesWritten
        )
    }

    private fun doIfEnabled(action: () -> Unit) {
        if (config.enabled) {
            action()
        }
    }
}