package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.domain.system.SystemLoad

interface SystemMetrics {
    fun systemLoad(
        sort: ProcessSort = ProcessSort.MEMORY,
        limit: Int = -1
    ): SystemLoad

    fun systemInfo(): SystemInfo
}