package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

@Entity
class CpuLoad(
    @Id
    val id: UUID,
    val usagePercentage: Double,
    val systemLoadAverage: Double,
    @OneToMany(mappedBy = "cpuLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    val coreLoads: List<CoreLoad>,
    @Embedded
    val cpuHealth: CpuHealth,
    val processCount: Int,
    val threadCount: Int
)