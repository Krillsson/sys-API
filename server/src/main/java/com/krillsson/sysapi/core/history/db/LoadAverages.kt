package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.Embeddable
import javax.persistence.Id
@Embeddable
data class LoadAverages(
    @Id
    val id: UUID,
    val oneMinutes: Double,
    val fiveMinutes: Double,
    val fifteenMinutes: Double
)