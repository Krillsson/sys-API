package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class NetworkInterfaceLoad(
    @Id
    open var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var history: HistorySystemLoadEntity? = null,
    open var historyId: UUID,
    open var name: String,
    open var mac: String,
    open var isUp: Boolean,
    @Embedded
    open var values: NetworkInterfaceValues,
    @Embedded
    open var speed: NetworkInterfaceSpeed
)

@Embeddable
class NetworkInterfaceValues(
    open var speed: Long,
    open var bytesReceived: Long,
    open var bytesSent: Long,
    open var packetsReceived: Long,
    open var packetsSent: Long,
    open var inErrors: Long,
    open var outErrors: Long
)

@Embeddable
class NetworkInterfaceSpeed(open var receiveBytesPerSecond: Long, open var sendBytesPerSecond: Long)

@Repository
interface NetworkLoadDAO : JpaRepository<NetworkInterfaceLoad, UUID> {
    fun findAllByHistoryId(id: UUID): List<NetworkInterfaceLoad>
}