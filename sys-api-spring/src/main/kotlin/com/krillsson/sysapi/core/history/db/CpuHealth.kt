package com.krillsson.sysapi.core.history.db

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
class CpuHealth(
    @Id
    open var id: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    open var temperatures: List<Double>,
    open var voltage: Double,
    open var fanRpm: Double,
    open var fanPercent: Double
)