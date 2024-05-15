package com.krillsson.sysapi.core.webservicecheck

import java.time.Instant
import java.util.*

data class WebServerCheck(
    val id: UUID,
    val url: String
)

class WebServerCheckHistoryEntry(
    val id: UUID,
    val webserverCheckId: UUID,
    val timeStamp: Instant,
    val responseCode: Int,
    val latencyMs: Long,
    val message: String,
    val errorBody: String?
) {
    val isSuccessful = responseCode in 200..299
}

class OneOffWebserverResult(
    val timeStamp: Instant,
    val responseCode: Int,
    val message: String,
    val latencyMs: Long,
    val errorBody: String?
)

sealed interface AddWebServerResult {
    data class Success(val id: UUID) : AddWebServerResult
    data class Fail(val reason: String) : AddWebServerResult
}

data class UptimeMetrics(
    val total: UptimePeriod,
    val perDay: List<UptimeDay>
)

data class UptimePeriod(
    val periodStart: Instant,
    val periodEnd: Instant,
    val totalDownTimeSeconds: Long,
    val totalUptimePercent: Double,
    val totalSeconds: Long
)

data class UptimeDay(
    val timestampAtStartOfDay: Instant,
    val uptimePercent: Double,
    val downTimeSeconds: Long,
    val totalSeconds: Long
)