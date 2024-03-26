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
    val id: UUID,
    @JoinColumn(name = "cpuLoadId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val cpuLoad: CpuLoad? = null,
    val cpuLoadId: UUID,
    val percentage: Double,
)