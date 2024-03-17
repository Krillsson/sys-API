package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.*

@Entity
class CoreLoad(
    @Id
    var id: UUID,
    @JoinColumn(name = "cpuLoadId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var cpuLoad: CpuLoad? = null,
    var cpuLoadId: UUID,
    var percentage: Double,
)