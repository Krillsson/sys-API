package com.krillsson.sysapi.core.history.db

import java.time.Instant
import java.util.*
import javax.persistence.*

// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
// https://stackoverflow.com/a/50378345
@Entity(name = "BasicHistorySystemLoadEntity")
@Table(name = "HistorySystemLoadEntity")
@NamedQuery(
    name = "com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity.findAll",
    query = "SELECT e FROM BasicHistorySystemLoadEntity e"
)
class BasicHistorySystemLoadEntity(
    @Id
    val id: UUID,
    val date: Instant,
    val uptime: Long,
    val systemLoadAverage: Double,
)

@Entity
@NamedQuery(
    name = "com.krillsson.sysapi.core.history.db.HistorySystemLoadEntity.findAll",
    query = "SELECT e FROM HistorySystemLoadEntity e"
)
class HistorySystemLoadEntity(
    @Id
    val id: UUID,
    val date: Instant,
    val uptime: Long,
    val systemLoadAverage: Double,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val cpuLoad: CpuLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val networkInterfaceLoads: List<NetworkInterfaceLoad>,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val connectivity: Connectivity,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val diskLoads: List<DiskLoad>?,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val fileSystemLoads: List<FileSystemLoad>?,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val memory: MemoryLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val gpuLoads: List<GpuLoad>,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    val motherboardHealth: List<HealthData>
)