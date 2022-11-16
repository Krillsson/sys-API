package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

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