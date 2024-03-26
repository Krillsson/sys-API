package com.krillsson.sysapi.core.history.db

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
class CpuHealth(
    @Id
    val id: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    val temperatures: List<Double>,
    val voltage: Double,
    val fanRpm: Double,
    val fanPercent: Double
)