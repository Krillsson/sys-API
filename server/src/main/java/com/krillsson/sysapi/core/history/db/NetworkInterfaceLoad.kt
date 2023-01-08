package com.krillsson.sysapi.core.history.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import java.util.*
import javax.persistence.*
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Entity
class NetworkInterfaceLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val mac: String,
    val isUp: Boolean,
    @Embedded
    val values: NetworkInterfaceValues,
    @Embedded
    val speed: NetworkInterfaceSpeed
)

@Embeddable
class NetworkInterfaceValues(
    val speed: Long,
    val bytesReceived: Long,
    val bytesSent: Long,
    val packetsReceived: Long,
    val packetsSent: Long,
    val inErrors: Long,
    val outErrors: Long
)

@Embeddable
class NetworkInterfaceSpeed(val receiveBytesPerSecond: Long, val sendBytesPerSecond: Long)

class NetworkLoadDAO(sessionFactory: SessionFactory) : AbstractDAO<NetworkInterfaceLoad>(sessionFactory) {
    fun findById(id: UUID): List<NetworkInterfaceLoad> {
        val builder = currentSession().criteriaBuilder
        val query: CriteriaQuery<NetworkInterfaceLoad> = builder.createQuery(NetworkInterfaceLoad::class.java)
        val root: Root<NetworkInterfaceLoad> = query.from(NetworkInterfaceLoad::class.java)
        val equals = builder.equal(root.get<UUID>("historyId"), id)
        return list(query.where(equals))
    }
}