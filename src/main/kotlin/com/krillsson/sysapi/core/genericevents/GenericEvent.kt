package com.krillsson.sysapi.core.genericevents

import com.krillsson.sysapi.core.monitoring.Monitor
import java.time.Instant
import java.util.*

sealed interface GenericEvent {
    val id: UUID
    val timestamp: Instant

    data class UpdateAvailable(
        override val id: UUID,
        override val timestamp: Instant,
        val currentVersion: String,
        val newVersion: String,
        val changeLogMarkdown: String,
        val downloadUrl: String,
        val publishDate: String,
    ) : GenericEvent

    data class MonitoredItemMissing(
        override val id: UUID,
        override val timestamp: Instant,
        val monitorType: Monitor.Type,
        val monitorId: UUID,
        val monitoredItemId: String?
    ) : GenericEvent
}