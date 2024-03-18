package com.krillsson.sysapi.core.history.db

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class HistorySystemLoadDAO {
    @PersistenceContext
    lateinit var em: EntityManager

    fun insert(entity: HistorySystemLoadEntity): UUID {
        em.persist(entity)
        return entity.id
    }

    fun insert(entities: List<HistorySystemLoadEntity>): List<UUID> {
        return entities.map {
            val createdId = insert(it)
            createdId
        }
    }

    fun findAllBetween(from: Instant, to: Instant): List<HistorySystemLoadEntity> {
        val builder = em.criteriaBuilder
        val query: CriteriaQuery<HistorySystemLoadEntity> = builder.createQuery(HistorySystemLoadEntity::class.java)
        val root: Root<HistorySystemLoadEntity> = query.from(HistorySystemLoadEntity::class.java)
        val between = builder.between(root.get("date"), from, to)
        return em.createQuery(query.where(between)).resultList
    }

    fun purge(maxAge: Instant): Int {
        val builder = em.criteriaBuilder
        val delete = builder.createCriteriaDelete(HistorySystemLoadEntity::class.java)
        val table = delete.from(HistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    fun findAll(): List<HistorySystemLoadEntity> {
        return em.createNamedQuery(
            "com.krillsson.sysapi.core.history.db.HistorySystemLoadEntity.findAll",
            HistorySystemLoadEntity::class.java
        ).resultList
    }
}

@Repository
class BasicHistorySystemLoadDAO {

    @PersistenceContext
    lateinit var em: EntityManager

    fun findAllBetween(from: Instant, to: Instant): List<BasicHistorySystemLoadEntity> {
        val builder = em.criteriaBuilder
        val query: CriteriaQuery<BasicHistorySystemLoadEntity> =
            builder.createQuery(BasicHistorySystemLoadEntity::class.java)
        val root: Root<BasicHistorySystemLoadEntity> = query.from(BasicHistorySystemLoadEntity::class.java)
        val between = builder.between(root.get("date"), from, to)
        return em.createQuery(query.where(between)).resultList
    }

    fun purge(maxAge: OffsetDateTime): Int {
        val builder = em.criteriaBuilder
        val delete = builder.createCriteriaDelete(BasicHistorySystemLoadEntity::class.java)
        val table = delete.from(BasicHistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    fun findAll(): List<BasicHistorySystemLoadEntity> {
        return em.createNamedQuery(
            "com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity.findAll",
            BasicHistorySystemLoadEntity::class.java
        ).resultList
    }
}
