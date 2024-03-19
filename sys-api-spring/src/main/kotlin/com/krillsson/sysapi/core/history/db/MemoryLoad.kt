package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class MemoryLoad(
    @Id
    val id: UUID,
    val numberOfProcesses: Int,
    val swapTotalBytes: Long,
    val swapUsedBytes: Long,
    val totalBytes: Long,
    val availableBytes: Long,
    val usedPercent: Double
)

@Repository
interface MemoryLoadDAO : JpaRepository<MemoryLoad, UUID>