package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuLoadMetrics
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.CentralProcessor
import oshi.software.os.OperatingSystem

@Component
@Lazy
class LinuxCpuLoadMetrics(
    processor: CentralProcessor,
    operatingSystem: OperatingSystem,
    cpuSensors: LinuxCpuSensors
) : DefaultCpuLoadMetrics(processor, operatingSystem, cpuSensors)