package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

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