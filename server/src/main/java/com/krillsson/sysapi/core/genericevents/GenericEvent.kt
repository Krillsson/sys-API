package com.krillsson.sysapi.core.genericevents

import com.krillsson.sysapi.core.monitoring.Monitor
import java.time.OffsetDateTime
import java.util.*

sealed interface GenericEvent {
    val id: UUID
    val dateTime: OffsetDateTime

    data class UpdateAvailable(
        override val id: UUID,
        override val dateTime: OffsetDateTime,
        val currentVersion: String,
        val newVersion: String,
        val changeLogMarkdown: String,
        val downloadUrl: String,
        val publishDate: String,
    ) : GenericEvent

    data class MonitoredItemMissing(
        override val id: UUID,
        override val dateTime: OffsetDateTime,
        val monitorType: Monitor.Type,
        val monitorId: UUID,
        val monitoredItemId: String?
    ) : GenericEvent
}