package com.krillsson.sysapi.core.domain.history

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.sensors.HealthData

data class HistorySystemLoad(
    val uptime: Long,
    val systemLoadAverage: Double,
    val cpuLoad: CpuLoad,
    val networkInterfaceLoads: List<NetworkInterfaceLoad>,
    val connectivity: Connectivity,
    val diskLoads: List<DiskLoad>,
    val fileSystemLoads: List<FileSystemLoad>,
    val memory: MemoryLoad,
    val gpuLoads: List<GpuLoad>,
    val motherboardHealth: List<HealthData>
)