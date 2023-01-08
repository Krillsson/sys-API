package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class HistorySystemLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<HistorySystemLoadEntity>(sessionFactory) {
    fun insert(entity: HistorySystemLoadEntity): UUID {
        return persist(entity).id
    }

    fun insert(entities: List<HistorySystemLoadEntity>): List<UUID> {
        return entities.map {
            val createdId = insert(it)
            createdId
        }
    }

    fun findAllBetween(from: OffsetDateTime, to: OffsetDateTime): List<HistorySystemLoadEntity> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<HistorySystemLoadEntity> = builder.createQuery(HistorySystemLoadEntity::class.java)
        val root: Root<HistorySystemLoadEntity> = query.from(HistorySystemLoadEntity::class.java)
        val between = builder.between(root.get("date"), from, to)
        return list(query.where(between))
    }

    fun purge(maxAge: OffsetDateTime): Int {
        val builder = currentSession().criteriaBuilder
        val delete = builder.createCriteriaDelete(HistorySystemLoadEntity::class.java)
        val table = delete.from(HistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return currentSession()
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    fun findAll(): List<HistorySystemLoadEntity> {
        return list(namedTypedQuery("com.krillsson.sysapi.core.history.db.HistorySystemLoadEntity.findAll"))
    }

    fun findById(id: UUID): HistorySystemLoadEntity? {
        return get(id)
    }
}

class BasicHistorySystemLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<BasicHistorySystemLoadEntity>(sessionFactory) {
    fun findAllBetween(from: OffsetDateTime, to: OffsetDateTime): List<BasicHistorySystemLoadEntity> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<BasicHistorySystemLoadEntity> = builder.createQuery(BasicHistorySystemLoadEntity::class.java)
        val root: Root<BasicHistorySystemLoadEntity> = query.from(BasicHistorySystemLoadEntity::class.java)
        val between = builder.between(root.get("date"), from, to)
        return list(query.where(between))
    }

    fun purge(maxAge: OffsetDateTime): Int {
        val builder = currentSession().criteriaBuilder
        val delete = builder.createCriteriaDelete(BasicHistorySystemLoadEntity::class.java)
        val table = delete.from(BasicHistorySystemLoadEntity::class.java)
        val lessThan = builder.lessThan(table.get("date"), maxAge)
        return currentSession()
            .createQuery(delete.where(lessThan))
            .executeUpdate()
    }

    fun findAll(): List<BasicHistorySystemLoadEntity> {
        return list(namedTypedQuery("com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity.findAll"))
    }

    fun findById(id: UUID): BasicHistorySystemLoadEntity? {
        return get(id)
    }
}
