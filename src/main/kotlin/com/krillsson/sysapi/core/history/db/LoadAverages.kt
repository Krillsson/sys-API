package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
data class LoadAverages(
    @Id
    val id: UUID,
    val oneMinutes: Double,
    val fiveMinutes: Double,
    val fifteenMinutes: Double
)