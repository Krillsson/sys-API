package com.krillsson.sysapi.core.domain.history

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

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
    @OneToOne
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val cpuLoad: CpuLoad,
    @OneToMany
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val networkInterfaceLoads: List<NetworkInterfaceLoad>,
    @OneToOne
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val connectivity: Connectivity,
    @OneToMany
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val driveLoads: List<DriveLoad>,
    @OneToOne
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val memory: MemoryLoad,
    @OneToMany
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val gpuLoads: List<GpuLoad>,
    @OneToMany
    @JoinColumn(name = "systemLoadId", insertable = false, updatable = false)
    val motherboardHealth: List<HealthData>
)

@Entity
class CoreLoad(
    @Id
    val id: UUID,
    val cpuLoadId: UUID,
    val percentage: Double,
)

@Entity
class CpuLoad(
    @Id
    val id: UUID,
    val systemLoadId: UUID,
    val usagePercentage: Double,
    val systemLoadAverage: Double,
    @OneToMany
    @JoinColumn(name = "cpuLoadId")
    val coreLoads: List<CoreLoad>,
    @OneToOne
    @JoinColumn(name = "cpuLoadId")
    val cpuHealth: CpuHealth,
    val processCount: Int,
    val threadCount: Int
)

@Entity
class CpuHealth(
    @Id
    val id: UUID,
    val cpuLoadId: UUID,
    @ElementCollection
    val temperatures: List<Double>,
    val voltage: Double,
    val fanRpm: Double,
    val fanPercent: Double
)

@Entity
class NetworkInterfaceLoad(
    @Id
    val id: UUID,
    val systemLoadId: UUID,
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
    val systemLoadId: UUID,
    val externalIp: String?,
    val previousExternalIp: String?,
    val connected: Boolean
)

@Entity
class DriveLoad(
    @Id
    val id: UUID,
    val systemLoadId: UUID,
    val name: String,
    val serial: String,
    @Embedded
    val values: DriveValues,
    @Embedded
    val speed: DriveSpeed,
    val temperature: Double,
    @OneToMany
    @JoinColumn(name = "driveLoadId")
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
    val systemLoadId: UUID,
    val description: String,
    val data: Double,
    val dataType: DataType
)

@Entity
class DriveHealthData(
    @Id
    val id: UUID,
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
    val systemLoadId: UUID,
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
    val systemLoadId: UUID,
    val name: String,
    val coreLoad: Double,
    val memoryLoad: Double,
    @Embedded
    val health: GpuHealth
)

@Embeddable
class GpuHealth(val fanRpm: Double, val fanPercent: Double, val temperature: Double)