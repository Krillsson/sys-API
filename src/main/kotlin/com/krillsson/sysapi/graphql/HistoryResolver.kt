package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity
import com.krillsson.sysapi.util.toOffsetDateTime
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.OffsetDateTime

@Component
class HistoryResolver(val historyRepository: HistoryRepository) : GraphQLResolver<BasicHistorySystemLoadEntity> {

    fun getDateTime(historyEntry: BasicHistorySystemLoadEntity): OffsetDateTime {
        return historyEntry.date.toOffsetDateTime()
    }

    fun getTimestamp(historyEntry: BasicHistorySystemLoadEntity): Instant {
        return historyEntry.date
    }

    fun getProcessorMetrics(historyEntry: BasicHistorySystemLoadEntity): CpuLoad {
        return historyRepository.getCpuLoadById(historyEntry.id)
    }

    fun getDiskMetrics(historyEntry: BasicHistorySystemLoadEntity): List<DiskLoad> {
        return historyRepository.getDiskLoadsById(historyEntry.id)
    }

    fun getFileSystemMetrics(historyEntry: BasicHistorySystemLoadEntity): List<FileSystemLoad> {
        return historyRepository.getFileSystemLoadsById(historyEntry.id)
    }

    fun getNetworkInterfaceMetrics(historyEntry: BasicHistorySystemLoadEntity): List<NetworkInterfaceLoad> {
        return historyRepository.getNetworkInterfaceLoadsById(historyEntry.id)
    }

    fun getConnectivity(historyEntry: BasicHistorySystemLoadEntity): Connectivity {
        return historyRepository.getConnectivityById(historyEntry.id)
    }

    fun getMemoryMetrics(historyEntry: BasicHistorySystemLoadEntity): MemoryLoad {
        return historyRepository.getMemoryLoadById(historyEntry.id)
    }
}