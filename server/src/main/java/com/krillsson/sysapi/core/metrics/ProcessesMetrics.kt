package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo
import java.util.Optional

interface ProcessesMetrics {
    fun processesInfo(sortBy: ProcessSort, limit: Int): ProcessesInfo
    fun getProcessByPid(pid: Int): Optional<Process>
}