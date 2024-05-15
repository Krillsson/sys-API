package com.krillsson.sysapi.core.webservicecheck

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant
import java.util.*

@Entity
class WebServerCheckEntity(
        @Id
        val id: UUID,
        val url: String
)

@Entity
class WebServerCheckHistoryEntity(
        @Id
        val id: UUID,
        val webServerCheckId: UUID,
        val timeStamp: Instant,
        val responseCode: Int,
        val latencyMs: Long,
        val message: String,
        val errorBody: String?
)



