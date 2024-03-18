package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
data class DiskLoad(
    @Id
    open var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var history: HistorySystemLoadEntity? = null,
    open var historyId: UUID,
    open var name: String,
    open var serial: String,
    @Embedded
    open var values: DiskValues,
    @Embedded
    open var speed: DiskSpeed,
)

@Embeddable
data class DiskValues(
    open var reads: Long,
    open var readBytes: Long,
    open var writes: Long,
    open var writeBytes: Long
)

@Embeddable
class DiskSpeed(
    open var readBytesPerSecond: Long,
    open var writeBytesPerSecond: Long
)

@Repository
interface DiskLoadDAO : JpaRepository<DiskLoad, UUID>{
    fun findAllByHistoryId(id: UUID): List<DiskLoad>
}