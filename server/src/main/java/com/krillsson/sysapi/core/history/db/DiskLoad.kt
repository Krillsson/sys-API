package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.util.*
import javax.persistence.*
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Entity
data class DiskLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val serial: String,
    @Embedded
    val values: DiskValues,
    @Embedded
    val speed: DiskSpeed,
)
@Embeddable
data class DiskValues(
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
)
@Embeddable
class DiskSpeed(
    val readBytesPerSecond: Long,
    val writeBytesPerSecond: Long
)

class DiskLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<DiskLoad>(sessionFactory) {
    fun findById(id: UUID): List<DiskLoad> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<DiskLoad> = builder.createQuery(DiskLoad::class.java)
        val root: Root<DiskLoad> = query.from(DiskLoad::class.java)
        val equals = builder.equal(root.get<UUID>("historyId"), id)
        return list(query.where(equals))
    }
}