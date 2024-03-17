package com.krillsson.sysapi.core.history.db

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.NamedQuery
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

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
    var id: UUID,
    var date: Instant,
    var uptime: Long,
    var systemLoadAverage: Double,
)

@Entity
@NamedQuery(
    name = "com.krillsson.sysapi.core.history.db.HistorySystemLoadEntity.findAll",
    query = "SELECT e FROM HistorySystemLoadEntity e"
)
class HistorySystemLoadEntity(
    @Id
    var id: UUID,
    var date: Instant,
    var uptime: Long,
    var systemLoadAverage: Double,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var cpuLoad: CpuLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var networkInterfaceLoads: List<NetworkInterfaceLoad>,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var connectivity: Connectivity,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var diskLoads: List<DiskLoad>?,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var fileSystemLoads: List<FileSystemLoad>?,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var memory: MemoryLoad,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var gpuLoads: List<GpuLoad>,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var motherboardHealth: List<HealthData>
)