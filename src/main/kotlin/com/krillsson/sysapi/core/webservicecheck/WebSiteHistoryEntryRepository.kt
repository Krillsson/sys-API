package com.krillsson.sysapi.core.webservicecheck

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*


@Repository
interface WebServerHistoryEntryRepository : JpaRepository<WebServerCheckHistoryEntity, UUID> {
    fun findByTimeStampBetweenAndWebServerCheckIdIs(from: Instant, to: Instant, websiteCheckId: UUID): List<WebServerCheckHistoryEntity>
    fun deleteByTimeStampBefore(beforeStamp: Instant): Int
    fun deleteByWebServerCheckId(websiteCheckId: UUID): Int
    fun findFirstByWebServerCheckIdOrderByTimeStampDesc(websiteCheckId: UUID): Optional<WebServerCheckHistoryEntity>
}