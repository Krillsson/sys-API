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
