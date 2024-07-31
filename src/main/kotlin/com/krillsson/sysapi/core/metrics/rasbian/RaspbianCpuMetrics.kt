package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuLoadMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

@Component
class RaspbianCpuMetrics(hal: HardwareAbstractionLayer, operatingSystem: OperatingSystem, cpuSensors: RaspbianCpuSensors, cpuLoadMetrics: DefaultCpuLoadMetrics) : DefaultCpuMetrics(hal, operatingSystem, cpuSensors, cpuLoadMetrics)