package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

@Component
@Lazy
class LinuxCpuMetrics(
    hal: HardwareAbstractionLayer,
    operatingSystem: OperatingSystem,
    cpuLoadMetrics: LinuxCpuLoadMetrics
) : DefaultCpuMetrics(hal, operatingSystem, cpuLoadMetrics)

