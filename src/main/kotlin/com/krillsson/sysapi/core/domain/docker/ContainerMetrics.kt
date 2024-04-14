package com.krillsson.sysapi.core.domain.docker

import java.time.Instant

data class ContainerMetricsHistoryEntry(
        val containerId: String,
        val timestamp: Instant,
        val metrics: ContainerMetrics
)

data class ContainerMetrics(
        val id: String,
        val cpuUsage: CpuUsage,
        val memoryUsage: MemoryUsage,
        val currentPid: Long,
        val networkUsage: NetworkUsage,
        val blockIOUsage: BlockIOUsage
)

data class CpuUsage(
        val usagePercentPerCore: Double,
        val usagePercentTotal: Double,
        val throttlingData: ThrottlingData
)

data class ThrottlingData(
        val periods: Long,
        val throttledPeriods: Long,
        val throttledTime: Long
)

data class NetworkUsage(
        val bytesReceived: Long,
        val bytesTransferred: Long,
)

data class BlockIOUsage(
        val bytesWritten: Long,
        val bytesRead: Long
)

data class MemoryUsage(
        val usageBytes: Long,
        val usagePercent: Double,
        val limitBytes: Long
)
