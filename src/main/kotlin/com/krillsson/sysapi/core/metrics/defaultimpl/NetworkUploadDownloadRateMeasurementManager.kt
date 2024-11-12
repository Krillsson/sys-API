package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class NetworkUploadDownloadRateMeasurementManager(clock: Clock) : SpeedMeasurementManager(clock)