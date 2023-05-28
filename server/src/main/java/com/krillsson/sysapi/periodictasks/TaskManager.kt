package com.krillsson.sysapi.periodictasks

import com.krillsson.sysapi.config.TasksConfiguration

class TaskManager(
    private val tasks: Map<TaskInterval, TasksConfiguration>,
    private val jobs: Map<TaskInterval, TaskExecutorJob>
) {

    fun registerTask(task: Task) {
        val interval = tasks
            .filterValues { (_, value) -> value.contains(task.key) }
            .keys
            .firstOrNull() ?: task.defaultInterval
        jobs[interval]?.register(task)
    }
}