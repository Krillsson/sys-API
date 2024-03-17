package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadDAO
import com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity
import com.krillsson.sysapi.core.history.db.ConnectivityDAO
import com.krillsson.sysapi.core.history.db.CpuLoadDAO
import com.krillsson.sysapi.core.history.db.DiskLoadDAO
import com.krillsson.sysapi.core.history.db.FileSystemLoadDAO
import com.krillsson.sysapi.core.history.db.HistorySystemLoadDAO
import com.krillsson.sysapi.core.history.db.MemoryLoadDAO
import com.krillsson.sysapi.core.history.db.NetworkLoadDAO
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Repository
open class HistoryRepository constructor(
    private val clock: Clock,
    private val dao: HistorySystemLoadDAO,
    private val basicDao: BasicHistorySystemLoadDAO,
    private val cpuLoadDAO: CpuLoadDAO,
    private val memoryLoadDAO: MemoryLoadDAO,
    private val networkLoadDAO: NetworkLoadDAO,
    private val diskLoadDAO: DiskLoadDAO,
    private val fileSystemLoadDAO: FileSystemLoadDAO,
    private val connectivityDAO: ConnectivityDAO
) {

    val logger by logger()

    @Transactional(readOnly = true)
    open fun get(): List<BasicHistorySystemLoadEntity> {
        return basicDao.findAll()
    }

    @Transactional(readOnly = true)
    open fun getExtended(): List<SystemHistoryEntry> {
        return getExtendedHistoryLimitedToDates(null, null)
    }

    @Transactional(readOnly = true)
    open fun getExtendedHistoryLimitedToDates(
        fromDate: Instant?,
        toDate: Instant?
    ): List<SystemHistoryEntry> {
        val result = measureTimeMillis {
            if (fromDate == null || toDate == null) {
                dao.findAll()
            } else {
                dao.findAllBetween(fromDate, toDate)
            }
        }
        logger.info(
            "Took {} to fetch {} history entries",
            "${result.first.toInt()}ms",
            result.second.size
        )
        return result.second.map { it.asSystemHistoryEntry() }
    }

    @Transactional
    open fun record(load: HistorySystemLoad) {
        val entry = SystemHistoryEntry(UUID.randomUUID(), clock.instant(), load)
        logger.trace("Recording history for {}", entry)
        dao.insert(entry.asEntity())
    }

    @Transactional
    open fun purge(olderThan: Long, unit: ChronoUnit) {
        val maxAge = clock.instant().minus(olderThan, unit)
        logger.info("Purging history older than {}", maxAge)
        dao.purge(maxAge)
    }

    @Transactional(readOnly = true)
    open fun getHistoryLimitedToDates(
        fromDate: Instant?,
        toDate: Instant?
    ): List<BasicHistorySystemLoadEntity> {
        val result = measureTimeMillis {
            if (fromDate == null || toDate == null) {
                basicDao.findAll()
            } else {
                basicDao.findAllBetween(fromDate, toDate)
            }
        }
        logger.info(
            "Took {} to fetch {} history entries",
            "${result.first.toInt()}ms",
            result.second.size
        )
        return result.second
    }

    @Transactional(readOnly = true)
    open fun getBasic(): List<BasicHistorySystemLoadEntity> {
        return getHistoryLimitedToDates(null, null)
    }

    @Transactional(readOnly = true)
    open fun getCpuLoadById(id: UUID): CpuLoad {
        return cpuLoadDAO.findById(id).get().asCpuLoad()
    }

    @Transactional(readOnly = true)
    open fun getMemoryLoadById(id: UUID): MemoryLoad {
        return memoryLoadDAO.findById(id).get().asMemoryLoad()
    }

    @Transactional(readOnly = true)
    open fun getConnectivityById(id: UUID): Connectivity {
        return connectivityDAO.findById(id).get().asConnectivity()
    }

    @Transactional(readOnly = true)
    open fun getNetworkInterfaceLoadsById(id: UUID): List<NetworkInterfaceLoad> {
        return networkLoadDAO.findAllByHistoryId(id).map { it.asNetworkInterfaceLoad() }
    }

    @Transactional(readOnly = true)
    open fun getDiskLoadsById(id: UUID): List<DiskLoad> {
        return diskLoadDAO.findAllByHistoryId(id).map { it.asDiskLoad() }
    }

    @Transactional(readOnly = true)
    open fun getFileSystemLoadsById(id: UUID): List<FileSystemLoad> {
        return fileSystemLoadDAO.findAllByHistoryId(id).map { it.asFileSystemLoad() }
    }
}