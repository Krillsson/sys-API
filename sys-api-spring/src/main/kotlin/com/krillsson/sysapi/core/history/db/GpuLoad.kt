package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.*

@Entity
class GpuLoad(
    @Id
    var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var history: HistorySystemLoadEntity? = null,
    var historyId: UUID,
    var name: String,
    var coreLoad: Double,
    var memoryLoad: Double,
    @Embedded
    var health: GpuHealth
)

@Embeddable
class GpuHealth(var fanRpm: Double, var fanPercent: Double, var temperature: Double)