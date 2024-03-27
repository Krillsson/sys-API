package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.graphql.domain.System
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import oshi.hardware.UsbDevice

@Component
class SystemResolver(val metrics: Metrics) : GraphQLResolver<System> {
    fun processorMetrics(system: System) = metrics.cpuMetrics().cpuLoad()

    fun getBaseboard(system: System): Motherboard {
        return metrics.motherboardMetrics().motherboard()
    }

    fun getHostname(system: System): String {
        return system.hostName
    }

    fun getUsbDevices(system: System): List<UsbDevice> {
        return getBaseboard(system).usbDevices.toList()
    }

    fun getUptime(system: System): Long {
        return metrics.cpuMetrics().uptime()
    }

    fun getConnectivity(system: System): Connectivity {
        return metrics.networkMetrics().connectivity()
    }

    fun getProcessor(system: System): CentralProcessor {
        return metrics.cpuMetrics().cpuInfo().centralProcessor
    }

    fun getGraphics(system: System): List<Gpu> {
        return metrics.gpuMetrics().gpus()
    }

    fun getDisks(system: System) = metrics.diskMetrics().disks()
    fun getDiskById(system: System, id: String) = metrics.diskMetrics().diskByName(id)
    fun getFileSystems(system: System) = metrics.fileSystemMetrics().fileSystems()
    fun getFileSystemById(system: System, id: String) = metrics.fileSystemMetrics().fileSystemById(id)

    fun getProcesses(
        system: System,
        limit: Int = 0,
        processSortMethod: ProcessSort = ProcessSort.MEMORY
    ): List<Process> {
        return metrics.processesMetrics()
            .processesInfo(processSortMethod, limit).processes
    }

    fun networkInterfaces(system: System) = metrics.networkMetrics().networkInterfaces()
    fun getNetworkInterfaceById(system: System, id: String) = metrics.networkMetrics().networkInterfaceById(id)
    fun getMemory(system: System) = metrics.memoryMetrics().memoryInfo()
}