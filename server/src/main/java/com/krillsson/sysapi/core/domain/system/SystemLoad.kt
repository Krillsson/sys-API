package com.krillsson.sysapi.core.domain.system

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.sensors.HealthData

class SystemLoad(
    val uptime: Long,
    val systemLoadAverage: Double,
    val cpuLoad: CpuLoad,
    val networkInterfaceLoads: List<NetworkInterfaceLoad>,
    val connectivity: Connectivity,
    val driveLoads: List<DriveLoad>,
    val diskLoads: List<DiskLoad>,
    val fileSystemLoads: List<FileSystemLoad>,
    val memory: MemoryLoad,
    val processes: List<Process>,
    val gpuLoads: List<GpuLoad>,
    val motherboardHealth: List<HealthData>
)