package com.krillsson.sysapi.core.history.db

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

@Repository
interface HistorySystemLoadDAO2 : JpaRepository<HistorySystemLoadEntity, UUID> {
    fun findAllByDateBetween(dateStart: Instant, dateEnd: Instant): List<HistorySystemLoadEntity>

    fun deleteAllByDateLessThan(date: Instant)
}

@Repository
interface BasicHistorySystemLoadDAO2 : JpaRepository<BasicHistorySystemLoadEntity, UUID> {
    fun findAllByDateBetween(dateStart: Instant, dateEnd: Instant): List<BasicHistorySystemLoadEntity>
    fun deleteAllByDateLessThan(date: Instant)
}

@Repository
class HistorySystemLoadDAO {
    @PersistenceContext
    lateinit var em: EntityManager

    @Transactional
    fun insert(entity: HistorySystemLoadEntity): UUID {
        em.persist(entity)
        return entity.id
    }

    @Transactional
    fun insert(entities: List<HistorySystemLoadEntity>): List<UUID> {
        return entities.map {
            val createdId = insert(it)
            createdId
        }
    }

    @Transactional
    fun purge(maxAge: Instant): Int {
        val builder = em.criteriaBuilder
        val delete = builder.createCriteriaDelete(HistorySystemLoadEntity::class.java)
        val table = delete.from(HistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    @Transactional(readOnly = true)
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

    @Transactional
    fun purge(maxAge: OffsetDateTime): Int {
        val builder = em.criteriaBuilder
        val delete = builder.createCriteriaDelete(BasicHistorySystemLoadEntity::class.java)
        val table = delete.from(BasicHistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return em
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<BasicHistorySystemLoadEntity> {
        return em.createNamedQuery(
            "com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity.findAll",
            BasicHistorySystemLoadEntity::class.java
        ).resultList
    }
}
