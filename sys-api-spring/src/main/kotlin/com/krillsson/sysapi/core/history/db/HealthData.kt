package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
class HealthData(
    @Id
    open var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var history: HistorySystemLoadEntity? = null,
    open var historyId: UUID,
    open var description: String,
    open var data: Double,
    open var dataType: DataType
)