package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

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