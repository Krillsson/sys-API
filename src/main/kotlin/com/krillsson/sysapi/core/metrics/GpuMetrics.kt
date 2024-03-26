package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import oshi.hardware.Display

interface GpuMetrics {
    fun gpus(): List<Gpu>
    fun displays(): List<Display>
    fun gpuLoads(): List<GpuLoad>
}