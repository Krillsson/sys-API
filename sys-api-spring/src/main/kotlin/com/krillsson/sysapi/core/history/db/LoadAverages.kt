package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Id
import java.util.UUID

@Embeddable
data class LoadAverages(
    @Id
    open var id: UUID,
    open var oneMinutes: Double,
    open var fiveMinutes: Double,
    open var fifteenMinutes: Double
)