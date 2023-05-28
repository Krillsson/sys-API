package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskManager
import java.time.Clock

class NetworkUploadDownloadRateMeasurementManager(clock: Clock, taskManager: TaskManager) :
    SpeedMeasurementManager(clock, Task.Key.RecordNetworkUploadDownloadRate) {
    init {
        taskManager.registerTask(this)
    }
}