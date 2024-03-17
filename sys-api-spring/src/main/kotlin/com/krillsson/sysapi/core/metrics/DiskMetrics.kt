package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.disk.Disk
import com.krillsson.sysapi.core.domain.disk.DiskLoad

interface DiskMetrics {
    fun disks(): List<Disk>
    fun diskLoads(): List<DiskLoad>
    fun diskLoadByName(name: String): DiskLoad?
    fun diskByName(name: String): Disk?
}