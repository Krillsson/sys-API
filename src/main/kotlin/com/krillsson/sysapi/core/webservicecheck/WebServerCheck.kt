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
    val message: String,
    val errorBody: String?
)

class OneOffWebserverResult(
        val timeStamp: Instant,
        val responseCode: Int,
        val message: String,
        val errorBody: String?
)

sealed interface AddWebServerResult {
    data class Success(val id: UUID) : AddWebServerResult
    data class Fail(val reason: String) : AddWebServerResult
}