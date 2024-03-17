package com.krillsson.sysapi.core.history.db

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Entity
class CpuLoad(
    @Id
    var id: UUID,
    var usagePercentage: Double,
    var systemLoadAverage: Double,
    @Embedded
    var loadAverages: LoadAverages,
    @OneToMany(mappedBy = "cpuLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    var coreLoads: List<CoreLoad>,
    @Embedded
    var cpuHealth: CpuHealth,
    var processCount: Int,
    var threadCount: Int
)

@Repository
interface CpuLoadDAO : JpaRepository<CpuLoad, UUID>