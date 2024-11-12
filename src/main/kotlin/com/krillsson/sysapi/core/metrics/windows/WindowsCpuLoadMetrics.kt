package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuLoadMetrics
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.CentralProcessor
import oshi.software.os.OperatingSystem

@Component
@Lazy
class WindowsCpuLoadMetrics(
    processor: CentralProcessor,
    operatingSystem: OperatingSystem,
    cpuSensors: WindowsCpuSensors
) : DefaultCpuLoadMetrics(processor, operatingSystem, cpuSensors)