package com.krillsson.sysapi.core.genericevents

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.krillsson.sysapi.core.monitoring.Monitor
import java.time.OffsetDateTime
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = GenericEvent.UpdateAvailable::class, name = "UpdateAvailable"),
    JsonSubTypes.Type(value = GenericEvent.MonitoredItemMissing::class, name = "MonitoredItemMissing"),
)
sealed interface GenericEvent {
    val type: String
        @JsonProperty("@type")
        get
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
    ) : GenericEvent {
        override val type: String = "UpdateAvailable"
    }

    data class MonitoredItemMissing(
        override val id: UUID,
        override val dateTime: OffsetDateTime,
        val monitorType: Monitor.Type,
        val monitorId: UUID,
        val monitoredItemId: String?
    ) : GenericEvent {
        override val type: String = "MonitoredItemMissing"
    }
}