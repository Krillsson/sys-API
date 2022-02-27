package com.krillsson.sysapi.core.domain.event

import com.krillsson.sysapi.core.monitoring.Monitor
import java.time.OffsetDateTime
import java.util.*

class OngoingEvent(
    id: UUID,
    monitorId: UUID,
    monitoredItemId: String?,
    monitorType: Monitor.Type,
    startTime: OffsetDateTime,
    threshold: Double,
    value: Double
) : Event(id, monitorId, monitoredItemId, monitorType, startTime, threshold, value)
