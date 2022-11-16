package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
class GpuLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val coreLoad: Double,
    val memoryLoad: Double,
    @Embedded
    val health: GpuHealth
)

@Embeddable
class GpuHealth(val fanRpm: Double, val fanPercent: Double, val temperature: Double)