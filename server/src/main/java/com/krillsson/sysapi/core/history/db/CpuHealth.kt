package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType
import javax.persistence.Id

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