package com.krillsson.sysapi.core.history.db

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
// https://stackoverflow.com/a/50378345
@MappedSuperclass
open class BasicHistorySystemLoadEntity(
    @Id
    val id: UUID,
    val date: OffsetDateTime,
    val uptime: Long,
    val systemLoadAverage: Double,
)


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(
    name = "com.krillsson.sysapi.core.history.db.HistorySystemLoadEntity.findAll",
    query = "SELECT e FROM HistorySystemLoadEntity e"
)
@NamedQuery(
    name = "com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity.findAll",
    query = "SELECT e FROM HistorySystemLoadEntity e"
)
class HistorySystemLoadEntity(
    id: UUID,
    date: OffsetDateTime,
    uptime: Long,
    systemLoadAverage: Double,
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
) : BasicHistorySystemLoadEntity(id, date, uptime, systemLoadAverage)