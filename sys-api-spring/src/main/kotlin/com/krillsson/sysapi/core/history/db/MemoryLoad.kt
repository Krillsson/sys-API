package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class MemoryLoad(
    @Id
    open var id: UUID,
    open var numberOfProcesses: Int,
    open var swapTotalBytes: Long,
    open var swapUsedBytes: Long,
    open var totalBytes: Long,
    open var availableBytes: Long,
    open var usedPercent: Double
)

@Repository
interface MemoryLoadDAO : JpaRepository<MemoryLoad, UUID>