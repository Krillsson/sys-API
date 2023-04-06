package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.util.*
import javax.persistence.*
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Entity
data class FileSystemLoad(
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

class FileSystemLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<FileSystemLoad>(sessionFactory) {
    fun findById(id: UUID): List<FileSystemLoad> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<FileSystemLoad> = builder.createQuery(FileSystemLoad::class.java)
        val root: Root<FileSystemLoad> = query.from(FileSystemLoad::class.java)
        val equals = builder.equal(root.get<UUID>("historyId"), id)
        return list(query.where(equals))
    }
}
