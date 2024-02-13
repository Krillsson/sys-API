package com.krillsson.sysapi.core.domain.docker

data class ContainerStatistics(
    val cpuUsage: CpuUsage,
    val memoryUsage: MemoryUsage,
    val currentPid: Long,
    val networkUsage: List<NetworkUsage>,
    val blockIOUsage: BlockIOUsage
)

data class CpuUsage(
    val totalUsage: Long,
    val perCpuUsage: List<Long>,
    val usageInKernelMode: Long,
    val usageInUserMode: Long,
    val throttlingData: ThrottlingData
)

data class ThrottlingData(
    val periods: Long,
    val throttledPeriods: Long,
    val throttledTime: Long
)

data class NetworkUsage(
    val name: String,
    val rxBytes: Long,
    val rxDropped: Long,
    val rxErrors: Long,
    val rxPackets: Long,
    val txBytes: Long,
    val txDropped: Long,
    val txErrors: Long,
    val txPackets: Long
)

data class BlockIOUsage(
    val ioServiceBytesRecursive: List<BlockIOEntry>,
    val ioServicedRecursive: List<BlockIOEntry>,
    val ioQueueRecursive: List<BlockIOEntry>,
    val ioServiceTimeRecursive: List<BlockIOEntry>,
    val ioWaitTimeRecursive: List<BlockIOEntry>,
    val ioMergedRecursive: List<BlockIOEntry>,
    val ioTimeRecursive: List<BlockIOEntry>,
    val sectorsRecursive: List<BlockIOEntry>
)

data class BlockIOEntry(
    val major: Long,
    val minor: Long,
    val operation: String,
    val value: Long
)

data class MemoryUsage(
    val usage: Long,
    val maxUsage: Long,
    val limit: Long
)
