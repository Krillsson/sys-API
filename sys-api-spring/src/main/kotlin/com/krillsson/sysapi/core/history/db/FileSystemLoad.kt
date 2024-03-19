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
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val fsId: String?,
    val freeSpaceBytes: Long,
    val usableSpaceBytes: Long,
    val totalSpaceBytes: Long
)

@Repository
interface FileSystemLoadDAO: JpaRepository<FileSystemLoad, UUID>{
    fun findAllByHistoryId(id: UUID): List<FileSystemLoad>
}
