package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
data class DiskLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val serial: String,
    @Embedded
    val values: DiskValues,
    @Embedded
    val speed: DiskSpeed,
)
@Embeddable
data class DiskValues(
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
)
@Embeddable
class DiskSpeed(
    val readBytesPerSecond: Long,
    val writeBytesPerSecond: Long
)