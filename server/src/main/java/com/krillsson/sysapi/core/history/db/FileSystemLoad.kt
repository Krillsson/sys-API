package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
data class FileSystemLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val freeSpaceBytes: Long,
    val usableSpaceBytes: Long,
    val totalSpaceBytes: Long
)