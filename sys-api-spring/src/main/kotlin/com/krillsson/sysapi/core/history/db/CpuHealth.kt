package com.krillsson.sysapi.core.history.db

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
class CpuHealth(
    @Id
    var id: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    var temperatures: List<Double>,
    var voltage: Double,
    var fanRpm: Double,
    var fanPercent: Double
)