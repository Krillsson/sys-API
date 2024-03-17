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
import java.util.*

@Entity
data class DiskLoad(
    @Id
    var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var history: HistorySystemLoadEntity? = null,
    var historyId: UUID,
    var name: String,
    var serial: String,
    @Embedded
    var values: DiskValues,
    @Embedded
    var speed: DiskSpeed,
)

@Embeddable
data class DiskValues(
    var reads: Long,
    var readBytes: Long,
    var writes: Long,
    var writeBytes: Long
)

@Embeddable
class DiskSpeed(
    var readBytesPerSecond: Long,
    var writeBytesPerSecond: Long
)

@Repository
interface DiskLoadDAO : JpaRepository<DiskLoad, UUID>{
    fun findAllByHistoryId(id: UUID): List<DiskLoad>
}