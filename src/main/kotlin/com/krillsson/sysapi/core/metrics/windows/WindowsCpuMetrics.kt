package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

@Component
@Lazy
class WindowsCpuMetrics(
    hal: HardwareAbstractionLayer,
    operatingSystem: OperatingSystem,
    windowsCpuLoadMetrics: WindowsCpuLoadMetrics
) : DefaultCpuMetrics(hal, operatingSystem, windowsCpuLoadMetrics)

