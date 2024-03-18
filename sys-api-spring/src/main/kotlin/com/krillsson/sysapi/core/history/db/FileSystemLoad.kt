package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class FileSystemLoad(
    @Id
    open var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var history: HistorySystemLoadEntity? = null,
    open var historyId: UUID,
    open var name: String,
    open var fsId: String?,
    open var freeSpaceBytes: Long,
    open var usableSpaceBytes: Long,
    open var totalSpaceBytes: Long
)

@Repository
interface FileSystemLoadDAO: JpaRepository<FileSystemLoad, UUID>{
    fun findAllByHistoryId(id: UUID): List<FileSystemLoad>
}
