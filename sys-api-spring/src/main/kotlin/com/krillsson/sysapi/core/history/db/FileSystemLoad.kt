package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.criteria.CriteriaQuery
import org.hibernate.SessionFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Entity
data class FileSystemLoad(
    @Id
    var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var history: HistorySystemLoadEntity? = null,
    var historyId: UUID,
    var name: String,
    var fsId: String?,
    var freeSpaceBytes: Long,
    var usableSpaceBytes: Long,
    var totalSpaceBytes: Long
)

@Repository
interface FileSystemLoadDAO: JpaRepository<FileSystemLoad, UUID>{
    fun findAllByHistoryId(id: UUID): List<FileSystemLoad>
}
