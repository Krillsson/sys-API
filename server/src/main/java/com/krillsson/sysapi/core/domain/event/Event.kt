package com.krillsson.sysapi.core.domain.event

import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.OffsetDateTime
import java.util.*

abstract class Event(
    val id: UUID,
    val monitorId: UUID,
    val monitoredItemId: String? = null,
    val monitorType: MonitorType,
    val startTime: OffsetDateTime,
    val threshold: Double,
    val value: Double
)