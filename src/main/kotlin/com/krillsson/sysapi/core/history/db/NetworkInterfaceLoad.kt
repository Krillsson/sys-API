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

@Repository
interface NetworkLoadDAO : JpaRepository<NetworkInterfaceLoad, UUID> {
    fun findAllByHistoryId(id: UUID): List<NetworkInterfaceLoad>
}