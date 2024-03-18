package com.krillsson.sysapi.core.history.db

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class CpuLoad(
    @Id
    open var id: UUID,
    open var usagePercentage: Double,
    open var systemLoadAverage: Double,
    @Embedded
    open var loadAverages: LoadAverages,
    @OneToMany(mappedBy = "cpuLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var coreLoads: List<CoreLoad>,
    @Embedded
    open var cpuHealth: CpuHealth,
    open var processCount: Int,
    open var threadCount: Int
)

@Repository
interface CpuLoadDAO : JpaRepository<CpuLoad, UUID>