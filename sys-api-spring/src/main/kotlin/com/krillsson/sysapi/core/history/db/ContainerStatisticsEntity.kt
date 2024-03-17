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
    lateinit var em: EntityManager

    fun insert(entity: ContainerStatisticsEntity): String {
        em.persist(entity)
        return entity.id
    }

    fun insertAll(entities: List<ContainerStatisticsEntity>): List<String> {
        return entities.map {
            var createdId = insert(it)
            createdId
        }
    }

    fun findAllBetween(id: String, from: Instant, to: Instant): List<ContainerStatisticsEntity> {
        var builder = em.criteriaBuilder
        var query: CriteriaQuery<ContainerStatisticsEntity> = builder.createQuery(ContainerStatisticsEntity::class.java)
        var root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        var between = builder
            .between(root.get("timestamp"), from, to)
        var equal = builder.equal(root.get<String>("id"), id)
        var condition = builder.and(between, equal)
        return em.createQuery(query.where(condition)).resultList
    }

    fun findLatest(id: String): ContainerStatisticsEntity? {
        var cb = em.criteriaBuilder
        var query: CriteriaQuery<ContainerStatisticsEntity> = cb.createQuery(ContainerStatisticsEntity::class.java)
        var root: Root<ContainerStatisticsEntity> = query.from(ContainerStatisticsEntity::class.java)
        var idPredicate = cb.equal(root.get<String>("id"), id)
        query.where(idPredicate)
        query.orderBy(cb.desc(root.get<Instant>("timestamp")))
        var results = em.createQuery(query)
            .setMaxResults(1) // Limit to only the first result
            .resultList
        return results.firstOrNull()
    }

    fun purge(maxAge: Instant): Int {
        var builder = em.criteriaBuilder
        var delete = builder.createCriteriaDelete(ContainerStatisticsEntity::class.java)
        var table = delete.from(ContainerStatisticsEntity::class.java)
        var lessThan = builder.lessThan(table.get("timestamp"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }
}

@Entity
class ContainerStatisticsEntity(
    @Id
    var id: String,
    var timestamp: Instant,
    var currentPid: Long,
    @Embedded
    var cpuUsage: CpuUsage,
    @Embedded
    var memoryUsage: MemoryUsage,
    @Embedded
    var networkUsage: NetworkUsage,
    @Embedded
    var blockIOUsage: BlockIOUsage
)

@Embeddable
data class CpuUsage(
    var usagePercentPerCore: Double,
    var usagePercentTotal: Double,
    var throttlingData: ThrottlingData
)

@Embeddable
data class ThrottlingData(
    var periods: Long,
    var throttledPeriods: Long,
    var throttledTime: Long
)

@Embeddable
data class NetworkUsage(
    var bytesReceived: Long,
    var bytesTransferred: Long,
)

@Embeddable
data class BlockIOUsage(
    var bytesWritten: Long,
    var bytesRead: Long
)

@Embeddable
data class MemoryUsage(
    var usageBytes: Long,
    var usagePercent: Double,
    var limitBytes: Long
)
