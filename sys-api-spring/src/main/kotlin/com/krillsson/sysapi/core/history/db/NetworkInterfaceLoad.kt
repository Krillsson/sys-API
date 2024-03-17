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
    var id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var history: HistorySystemLoadEntity? = null,
    var historyId: UUID,
    var name: String,
    var mac: String,
    var isUp: Boolean,
    @Embedded
    var values: NetworkInterfaceValues,
    @Embedded
    var speed: NetworkInterfaceSpeed
)

@Embeddable
class NetworkInterfaceValues(
    var speed: Long,
    var bytesReceived: Long,
    var bytesSent: Long,
    var packetsReceived: Long,
    var packetsSent: Long,
    var inErrors: Long,
    var outErrors: Long
)

@Embeddable
class NetworkInterfaceSpeed(var receiveBytesPerSecond: Long, var sendBytesPerSecond: Long)

@Repository
interface NetworkLoadDAO : JpaRepository<NetworkInterfaceLoad, UUID> {
    fun findAllByHistoryId(id: UUID): List<NetworkInterfaceLoad>
}