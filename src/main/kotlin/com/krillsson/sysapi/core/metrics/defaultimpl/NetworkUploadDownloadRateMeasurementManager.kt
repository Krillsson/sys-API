package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Clock
import java.util.concurrent.TimeUnit

@Component
class NetworkUploadDownloadRateMeasurementManager(clock: Clock) :
    SpeedMeasurementManager(clock) {

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    fun runMeasurement() {
        run()
    }
}