package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.util.*
import javax.persistence.*

@Entity
class CpuLoad(
    @Id
    val id: UUID,
    val usagePercentage: Double,
    val systemLoadAverage: Double,
    @Embedded
    val loadAverages: LoadAverages,
    @OneToMany(mappedBy = "cpuLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    val coreLoads: List<CoreLoad>,
    @Embedded
    val cpuHealth: CpuHealth,
    val processCount: Int,
    val threadCount: Int
)

class CpuLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<CpuLoad>(sessionFactory) {
    fun findById(id: UUID): CpuLoad {
        return get(id)
    }
}