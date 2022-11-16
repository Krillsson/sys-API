@file:Suppress("JpaAttributeMemberSignatureInspection")

package com.krillsson.sysapi.core.domain.history

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
// https://stackoverflow.com/a/50378345
@Entity
@NamedQuery(
    name = "com.krillsson.sysapi.core.domain.history.HistorySystemLoadEntity.findAll",
    query = "SELECT e FROM HistorySystemLoadEntity e"
)
class HistorySystemLoadEntity(
    @Id
    val id: UUID,
    val date: OffsetDateTime,
    val uptime: Long,
    val systemLoadAverage: Double,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val cpuLoad: CpuLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val networkInterfaceLoads: List<NetworkInterfaceLoad>,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val connectivity: Connectivity,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val driveLoads: List<DriveLoad>,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val memory: MemoryLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val gpuLoads: List<GpuLoad>,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val motherboardHealth: List<HealthData>
)

@Entity
class CpuLoad(
    @Id
    val id: UUID,
    val usagePercentage: Double,
    val systemLoadAverage: Double,
    @OneToMany(mappedBy = "cpuLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    val coreLoads: List<CoreLoad>,
    @Embedded
    val cpuHealth: CpuHealth,
    val processCount: Int,
    val threadCount: Int
)

@Entity
class CoreLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "cpuLoadId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val cpuLoad: CpuLoad? = null,
    val cpuLoadId: UUID,
    val percentage: Double,
)

@Embeddable
class CpuHealth(
    @Id
    val id: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    val temperatures: List<Double>,
    val voltage: Double,
    val fanRpm: Double,
    val fanPercent: Double
)

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

@Entity
class Connectivity(
    @Id
    val id: UUID,
    val externalIp: String?,
    val previousExternalIp: String?,
    val connected: Boolean
)

@Entity
class DriveLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val serial: String,
    @Embedded
    val values: DriveValues,
    @Embedded
    val speed: DriveSpeed,
    val temperature: Double,
    @OneToMany(mappedBy = "driveLoad", cascade = [CascadeType.ALL], orphanRemoval = true)
    val healthData: List<DriveHealthData>
)

@Embeddable
class DriveValues(
    val usableSpace: Long,
    val totalSpace: Long,
    val openFileDescriptors: Long,
    val maxFileDescriptors: Long,
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
)

@Embeddable
class DriveSpeed(val readBytesPerSecond: Long, val writeBytesPerSecond: Long)

@Entity
class HealthData(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val description: String,
    val data: Double,
    val dataType: DataType
)

@Entity
class DriveHealthData(
    @Id
    val id: UUID,
    @JoinColumn(name = "driveLoadId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val driveLoad: DriveLoad? = null,
    val driveLoadId: UUID,
    val description: String,
    val data: Double,
    val dataType: DataType
)

enum class DataType {
    CLOCK, VOLTAGE, PERCENT, RPM, CELCIUS, GIGABYTE
}


@Entity
class MemoryLoad(
    @Id
    val id: UUID,
    val numberOfProcesses: Int,
    val swapTotalBytes: Long,
    val swapUsedBytes: Long,
    val totalBytes: Long,
    val availableBytes: Long,
    val usedPercent: Double
)

@Entity
class GpuLoad(
    @Id
    val id: UUID,
    @JoinColumn(name = "historyId", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val history: HistorySystemLoadEntity? = null,
    val historyId: UUID,
    val name: String,
    val coreLoad: Double,
    val memoryLoad: Double,
    @Embedded
    val health: GpuHealth
)

@Embeddable
class GpuHealth(val fanRpm: Double, val fanPercent: Double, val temperature: Double)