package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
class GpuLoad(
    @Id
    open var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var history: HistorySystemLoadEntity? = null,
    open var historyId: UUID,
    open var name: String,
    open var coreLoad: Double,
    open var memoryLoad: Double,
    @Embedded
    open var health: GpuHealth
)

@Embeddable
class GpuHealth(open var fanRpm: Double, open var fanPercent: Double, open var temperature: Double)