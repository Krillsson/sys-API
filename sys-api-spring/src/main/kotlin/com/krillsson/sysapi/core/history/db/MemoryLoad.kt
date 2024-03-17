package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class MemoryLoad(
    @Id
    var id: UUID,
    var numberOfProcesses: Int,
    var swapTotalBytes: Long,
    var swapUsedBytes: Long,
    var totalBytes: Long,
    var availableBytes: Long,
    var usedPercent: Double
)

@Repository
interface MemoryLoadDAO : JpaRepository<MemoryLoad, UUID>