package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.time.Instant
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


class ContainerStatisticsDAO(sessionFactory: SessionFactory) : AbstractDAO<ContainerStatisticsEntity>(sessionFactory) {
    fun insert(entity: ContainerStatisticsEntity): String {
        return persist(entity).id
    }

    fun insertAll(entities: List<ContainerStatisticsEntity>): List<String> {
        return entities.map {
            val createdId = insert(it)
            createdId
        }
    }

    fun findAllBetween(id: String, from: Instant, to: Instant): List<ContainerStatisticsEntity> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<ContainerStatisticsEntity> = builder.createQuery(ContainerStatisticsEntity::class.java)
        val root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        val between = builder
            .between(root.get("timestamp"), from, to)
        val equal = builder.equal(root.get<String>("id"), id)
        val condition = builder.and(between, equal)
        return list(query.where(condition))
    }

    fun findLatest(id: String): ContainerStatisticsEntity? {
        val cb = currentSession().criteriaBuilder
        val query: CriteriaQuery<ContainerStatisticsEntity> = cb.createQuery(ContainerStatisticsEntity::class.java)
        val root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        val idPredicate = cb.equal(root.get<String>("id"), id)
        query.where(idPredicate)
        query.orderBy(cb.desc(root.get<Instant>("timestamp")))
        val results = currentSession().createQuery(query)
            .setMaxResults(1) // Limit to only the first result
            .resultList
        return results.firstOrNull()
    }

    fun purge(maxAge: Instant): Int {
        val builder = currentSession().criteriaBuilder
        val delete = builder.createCriteriaDelete(ContainerStatisticsEntity::class.java)
        val table = delete.from(ContainerStatisticsEntity::class.java)
        val lessThan = builder.lessThan(table.get("timestamp"), maxAge)
        return currentSession()
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }
}

@Entity
class ContainerStatisticsEntity(
    @Id
    val id: String,
    val timestamp: Instant,
    val currentPid: Long,
    @Embedded
    val cpuUsage: CpuUsage,
    @Embedded
    val memoryUsage: MemoryUsage,
    @Embedded
    val networkUsage: NetworkUsage,
    @Embedded
    val blockIOUsage: BlockIOUsage
)

@Embeddable
data class CpuUsage(
    val usagePercentPerCore: Double,
    val usagePercentTotal: Double,
    val throttlingData: ThrottlingData
)

@Embeddable
data class ThrottlingData(
    val periods: Long,
    val throttledPeriods: Long,
    val throttledTime: Long
)

@Embeddable
data class NetworkUsage(
    val bytesReceived: Long,
    val bytesTransferred: Long,
)

@Embeddable
data class BlockIOUsage(
    val bytesWritten: Long,
    val bytesRead: Long
)

@Embeddable
data class MemoryUsage(
    val usageBytes: Long,
    val usagePercent: Double,
    val limitBytes: Long
)
