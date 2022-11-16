package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
class HealthData(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val description: String,
    val data: Double,
    val dataType: DataType
)