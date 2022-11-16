package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
class DriveLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val serial: String,
    @Embedded
    val values: DriveValues,
    @Embedded
    val speed: DriveSpeed,
    val temperature: Double,
    @OneToMany(mappedBy = "driveLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    val healthData: List<DriveHealthData>
)

@Embeddable
class DriveValues(
    val usableSpace: Long,
    val totalSpace: Long,
    val openFileDescriptors: Long,
    val maxFileDescriptors: Long,
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
)

@Embeddable
class DriveSpeed(val readBytesPerSecond: Long, val writeBytesPerSecond: Long)