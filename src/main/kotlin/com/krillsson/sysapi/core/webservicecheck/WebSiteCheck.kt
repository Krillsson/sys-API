package com.krillsson.sysapi.core.webservicecheck

import java.time.Instant
import java.util.*

data class WebServerCheck(
        val id: UUID,
        val url: String
)

class WebServerCheckHistoryEntry(
        val id: UUID,
        val webServerCheckId: UUID,
        val timeStamp: Instant,
        val responseCode: Int,
        val message: String,
        val errorBody: String?
)