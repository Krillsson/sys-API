package com.krillsson.sysapi.core.history

import com.google.common.eventbus.EventBus
import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.time.OffsetDateTime
import java.util.stream.Collectors

class MetricsHistoryManager(configuration: HistoryConfiguration, eventBus: EventBus) :
    HistoryManager(configuration, eventBus) {
    fun cpuLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<CpuLoad>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: HistoryEntry<SystemLoad> ->
                HistoryEntry(
                    e.date,
                    e.value.cpuLoad
                )
            }
            .collect(Collectors.toList())
    }

    fun systemLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<SystemLoad>> {
        return getHistoryLimitedToDates(fromDate, toDate)
    }

    fun driveLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<DriveLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: HistoryEntry<SystemLoad> ->
                HistoryEntry(
                    e.date,
                    e.value.driveLoads
                )
            }
            .collect(Collectors.toList())
    }

    fun gpuLoadHistory(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<List<GpuLoad>>> {
        return systemLoadHistory(fromDate, toDate).stream()
            .map { e: HistoryEntry<SystemLoad> ->
                HistoryEntry(
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
            .map { e: HistoryEntry<SystemLoad> ->
                HistoryEntry(
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
            .map { e: HistoryEntry<SystemLoad> ->
                HistoryEntry(
                    e.date,
                    e.value.networkInterfaceLoads
                )
            }
            .collect(Collectors.toList())
    }
}