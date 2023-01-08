package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.history.db.*
import com.krillsson.sysapi.util.Clock
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import io.dropwizard.hibernate.UnitOfWork
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

open class HistoryRepository constructor(
    private val clock: Clock,
    private val dao: HistorySystemLoadDAO,
    private val basicDao: BasicHistorySystemLoadDAO,
    private val cpuLoadDAO: CpuLoadDAO,
    private val memoryLoadDAO: MemoryLoadDAO,
    private val networkLoadDAO: NetworkLoadDAO,
    private val driveLoadDAO: DriveLoadDAO,
    private val connectivityDAO: ConnectivityDAO
) {

    val logger by logger()

    @UnitOfWork
    open fun get(): List<BasicHistorySystemLoadEntity> {
        return basicDao.findAll()
    }

    @UnitOfWork
    open fun getExtended(): List<SystemHistoryEntry> {
        return dao.findAll().map { it.asSystemHistoryEntry() }
    }

    @UnitOfWork
    open fun getExtendedHistoryLimitedToDates(
        fromDate: OffsetDateTime,
        toDate: OffsetDateTime
    ): List<SystemHistoryEntry> {
        return dao.findAllBetween(fromDate, toDate).map { it.asSystemHistoryEntry() }
    }

    @UnitOfWork
    open fun record(load: HistorySystemLoad) {
        val entry = SystemHistoryEntry(UUID.randomUUID(), clock.now(), load)
        logger.trace("Recording history for {}", entry)
        dao.insert(entry.asEntity())
    }

    @UnitOfWork
    open fun purge(olderThan: Long, unit: ChronoUnit) {
        val maxAge = clock.now().minus(olderThan, unit)
        logger.info("Purging history older than {}", maxAge)
        dao.purge(maxAge)
    }

    @UnitOfWork
    open fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
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

    @UnitOfWork
    open fun getBasic(): List<BasicHistorySystemLoadEntity> {
        return getHistoryLimitedToDates(null, null)
    }

    @UnitOfWork
    open fun getCpuLoadById(id: UUID): CpuLoad {
        return cpuLoadDAO.findById(id).asCpuLoad()
    }

    @UnitOfWork
    open fun getMemoryLoadById(id: UUID): MemoryLoad {
        return memoryLoadDAO.findById(id).asMemoryLoad()
    }

    @UnitOfWork
    open fun getConnectivityById(id: UUID): Connectivity {
        return connectivityDAO.findById(id).asConnectivity()
    }

    @UnitOfWork
    open fun getNetworkInterfaceLoadsById(id: UUID): List<NetworkInterfaceLoad> {
        return networkLoadDAO.findById(id).map { it.asNetworkInterfaceLoad() }
    }

    @UnitOfWork
    open fun getDriveLoadsById(id: UUID): List<DriveLoad> {
        return driveLoadDAO.findById(id).map { it.asDriveLoad() }
    }

}