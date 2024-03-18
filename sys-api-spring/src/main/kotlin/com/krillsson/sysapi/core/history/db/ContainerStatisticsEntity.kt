package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.Id
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class ContainerStatisticsDAO {

    @PersistenceContext
    lateinit open var em: EntityManager

    fun insert(entity: ContainerStatisticsEntity): String {
        em.persist(entity)
        return entity.id
    }

    fun insertAll(entities: List<ContainerStatisticsEntity>): List<String> {
        return entities.map {
            val createdId = insert(it)
            createdId
        }
    }

    fun findAllBetween(id: String, from: Instant, to: Instant): List<ContainerStatisticsEntity> {
        val builder = em.criteriaBuilder
        val query: CriteriaQuery<ContainerStatisticsEntity> = builder.createQuery(ContainerStatisticsEntity::class.java)
        val root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        val between = builder
            .between(root.get("timestamp"), from, to)
        val equal = builder.equal(root.get<String>("id"), id)
        val condition = builder.and(between, equal)
        return em.createQuery(query.where(condition)).resultList
    }

    fun findLatest(id: String): ContainerStatisticsEntity? {
        val cb = em.criteriaBuilder
        val query: CriteriaQuery<ContainerStatisticsEntity> = cb.createQuery(ContainerStatisticsEntity::class.java)
        val root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        val idPredicate = cb.equal(root.get<String>("id"), id)
        query.where(idPredicate)
        query.orderBy(cb.desc(root.get<Instant>("timestamp")))
        val results = em.createQuery(query)
            .setMaxResults(1) // Limit to only the first result
            .resultList
        return results.firstOrNull()
    }

    fun purge(maxAge: Instant): Int {
        val builder = em.criteriaBuilder
        val delete = builder.createCriteriaDelete(ContainerStatisticsEntity::class.java)
        val table = delete.from(ContainerStatisticsEntity::class.java)
        val lessThan = builder.lessThan(table.get("timestamp"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }
}

@Entity
class ContainerStatisticsEntity(
    @Id
    open var id: String,
    open var timestamp: Instant,
    open var currentPid: Long,
    @Embedded
    open var cpuUsage: CpuUsage,
    @Embedded
    open var memoryUsage: MemoryUsage,
    @Embedded
    open var networkUsage: NetworkUsage,
    @Embedded
    open var blockIOUsage: BlockIOUsage
)

@Embeddable
data class CpuUsage(
    open var usagePercentPerCore: Double,
    open var usagePercentTotal: Double,
    open var throttlingData: ThrottlingData
)

@Embeddable
data class ThrottlingData(
    open var periods: Long,
    open var throttledPeriods: Long,
    open var throttledTime: Long
)

@Embeddable
data class NetworkUsage(
    open var bytesReceived: Long,
    open var bytesTransferred: Long,
)

@Embeddable
data class BlockIOUsage(
    open var bytesWritten: Long,
    open var bytesRead: Long
)

@Embeddable
data class MemoryUsage(
    open var usageBytes: Long,
    open var usagePercent: Double,
    open var limitBytes: Long
)
