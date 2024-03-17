package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
data class LoadAverages(
    @Id
    var id: UUID,
    var oneMinutes: Double,
    var fiveMinutes: Double,
    var fifteenMinutes: Double
)