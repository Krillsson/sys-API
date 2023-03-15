package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import java.time.OffsetDateTime
import java.util.stream.Collectors

class LegacyHistoryManager(
    private val history: HistoryRepository
) {
    fun cpuLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<CpuLoad>> {
        return systemLoadHistory(fromDate, toDate)
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.cpuLoad
                )
            }
    }

    fun systemLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<SystemHistoryEntry> {
        return getHistoryLimitedToDates(fromDate, toDate)
    }

    fun driveLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<DriveLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.driveLoads
                )
            }
            .collect(Collectors.toList())
    }

    fun diskLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<DiskLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.diskLoads
                )
            }
            .collect(Collectors.toList())
    }

    fun fileSystemLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<FileSystemLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.fileSystemLoads
                )
            }
            .collect(Collectors.toList())
    }

    fun gpuLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<GpuLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.gpuLoads
                )
            }
            .collect(Collectors.toList())
    }

    fun memoryHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<MemoryLoad>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.memory
                )
            }
            .collect(Collectors.toList())
    }

    fun networkInterfaceLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<NetworkInterfaceLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: SystemHistoryEntry ->
                HistoryEntry(
                    e.id,
                    e.date,
                    e.value.networkInterfaceLoads
                )
            }
            .collect(Collectors.toList())
    }

    val logger by logger()

    fun getHistory(): List<SystemHistoryEntry> {
        return getHistoryLimitedToDates(null, null)
    }

    fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<SystemHistoryEntry> {
        val result = measureTimeMillis {
            if (fromDate == null || toDate == null) {
                history.getExtended()
            } else {
                history.getExtendedHistoryLimitedToDates(fromDate, toDate)
            }
        }
        logger.info(
            "Took {} to fetch {} history entries",
            "${result.first.toInt()}ms",
            result.second.size
        )
        return result.second
    }
}

