package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.util.asOshiProcessSort
import oshi.hardware.GlobalMemory
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSProcess
import oshi.software.os.OperatingSystem
import java.util.Optional

class DefaultProcessesMetrics(
    private val operatingSystem: OperatingSystem,
    private val hal: HardwareAbstractionLayer
) : ProcessesMetrics {
    override fun getProcessByPid(pid: Int): Optional<Process> {
        return Optional
            .of(operatingSystem.getProcess(pid))
            .map { process: OSProcess ->
                process.asProcess(hal.memory.total)
            }
    }

    override fun processesInfo(sortBy: ProcessSort, limit: Int): ProcessesInfo {
        val totalBytes = hal.memory.total
        var processes: List<Process> = emptyList()
        if (limit > -1) {
            processes = operatingSystem.getProcesses(limit, sortBy.asOshiProcessSort())
                .map { process: OSProcess ->
                    process.asProcess(totalBytes)
                }
        }
        return ProcessesInfo(
            operatingSystem.processId.toLong(),
            operatingSystem.threadCount.toLong(),
            operatingSystem.processCount.toLong(),
            processes
        )
    }

    private fun OSProcess.asProcess(totalBytes: Long): Process {
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
            100.0 * (kernelTime + userTime) / upTime,
            startTime,
            bytesRead,
            bytesWritten
        )
    }

    private fun usedPercent(memory: GlobalMemory): Int {
        val free = memory.available
        val total = memory.total
        val used = total - free
        return (used * 100.0 / total + 0.5).toInt()
    }
}