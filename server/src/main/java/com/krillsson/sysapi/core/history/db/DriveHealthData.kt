package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.*

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