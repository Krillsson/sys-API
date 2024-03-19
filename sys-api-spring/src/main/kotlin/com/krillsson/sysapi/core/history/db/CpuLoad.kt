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

@Repository
interface CpuLoadDAO : JpaRepository<CpuLoad, UUID>