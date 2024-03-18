package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
class CoreLoad(
    @Id
    open var id: UUID,
    @JoinColumn(name = "cpuLoadId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var cpuLoad: CpuLoad? = null,
    open var cpuLoadId: UUID,
    open var percentage: Double,
)