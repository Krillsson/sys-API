package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Connectivity(
    @Id
    val id: UUID,
    val externalIp: String?,
    val previousExternalIp: String?,
    val localIp: String?,
    val connected: Boolean
)

class ConnectivityDAO(sessionFactory: SessionFactory) : AbstractDAO<Connectivity>(sessionFactory) {
    fun findById(id: UUID): Connectivity {
        return get(id)
    }
}